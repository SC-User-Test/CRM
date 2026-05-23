# Azure Deployment Guide

## Overview

This guide provides step-by-step instructions for deploying the cloud-ready CRM application to Azure. The application has been transformed to use Azure-native services and follows cloud-native patterns.

## Prerequisites

- Azure subscription with appropriate permissions
- Azure CLI installed and configured
- Maven 3.6+ for building the application
- Java 8 or higher

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     Azure Cloud Platform                     │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────────────┐         ┌──────────────────┐          │
│  │  Azure Container │         │   Azure Blob     │          │
│  │   Apps / App     │────────▶│    Storage       │          │
│  │     Service      │         │                  │          │
│  └────────┬─────────┘         └──────────────────┘          │
│           │                                                   │
│           │                   ┌──────────────────┐          │
│           ├──────────────────▶│  Azure Database  │          │
│           │                   │   for MySQL      │          │
│           │                   └──────────────────┘          │
│           │                                                   │
│           │                   ┌──────────────────┐          │
│           ├──────────────────▶│   Azure App      │          │
│           │                   │  Configuration   │          │
│           │                   └──────────────────┘          │
│           │                                                   │
│           │                   ┌──────────────────┐          │
│           └──────────────────▶│  Azure Key Vault │          │
│                               │                  │          │
│                               └──────────────────┘          │
│                                                               │
│  ┌──────────────────┐         ┌──────────────────┐          │
│  │  Azure Logic     │         │  Application     │          │
│  │     Apps         │         │    Insights      │          │
│  │  (Scheduling)    │         │  (Monitoring)    │          │
│  └──────────────────┘         └──────────────────┘          │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

## Step 1: Create Azure Resources

### 1.1 Create Resource Group

```bash
# Set variables
RESOURCE_GROUP="crm-app-rg"
LOCATION="eastus"

# Create resource group
az group create \
  --name $RESOURCE_GROUP \
  --location $LOCATION
```

### 1.2 Create Azure Storage Account

```bash
STORAGE_ACCOUNT="crmappstorage$(date +%s)"

# Create storage account
az storage account create \
  --name $STORAGE_ACCOUNT \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --sku Standard_LRS \
  --kind StorageV2

# Create blob container
az storage container create \
  --name crm-files \
  --account-name $STORAGE_ACCOUNT \
  --auth-mode login
```

### 1.3 Create Azure Database for MySQL

```bash
DB_SERVER="crm-mysql-server-$(date +%s)"
DB_NAME="crmdb"
DB_ADMIN="crmadmin"
DB_PASSWORD="YourSecurePassword123!"

# Create MySQL server
az mysql flexible-server create \
  --resource-group $RESOURCE_GROUP \
  --name $DB_SERVER \
  --location $LOCATION \
  --admin-user $DB_ADMIN \
  --admin-password $DB_PASSWORD \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --version 8.0.21 \
  --storage-size 32 \
  --public-access 0.0.0.0

# Create database
az mysql flexible-server db create \
  --resource-group $RESOURCE_GROUP \
  --server-name $DB_SERVER \
  --database-name $DB_NAME
```

### 1.4 Create Azure Key Vault

```bash
KEY_VAULT="crm-keyvault-$(date +%s)"

# Create Key Vault
az keyvault create \
  --name $KEY_VAULT \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --enable-rbac-authorization false

# Store secrets
az keyvault secret set \
  --vault-name $KEY_VAULT \
  --name "db-password" \
  --value "$DB_PASSWORD"

az keyvault secret set \
  --vault-name $KEY_VAULT \
  --name "storage-connection-string" \
  --value "$(az storage account show-connection-string \
    --name $STORAGE_ACCOUNT \
    --resource-group $RESOURCE_GROUP \
    --query connectionString -o tsv)"
```

### 1.5 Create Azure App Configuration (Optional)

```bash
APP_CONFIG="crm-appconfig-$(date +%s)"

# Create App Configuration
az appconfig create \
  --name $APP_CONFIG \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --sku Standard

# Add configuration values
az appconfig kv set \
  --name $APP_CONFIG \
  --key "app.timezone" \
  --value "UTC" \
  --yes

az appconfig kv set \
  --name $APP_CONFIG \
  --key "app.display-timezone" \
  --value "UTC" \
  --yes
```

### 1.6 Create Application Insights

```bash
APP_INSIGHTS="crm-appinsights"

# Create Application Insights
az monitor app-insights component create \
  --app $APP_INSIGHTS \
  --location $LOCATION \
  --resource-group $RESOURCE_GROUP \
  --application-type web

# Get instrumentation key
INSTRUMENTATION_KEY=$(az monitor app-insights component show \
  --app $APP_INSIGHTS \
  --resource-group $RESOURCE_GROUP \
  --query instrumentationKey -o tsv)
```

## Step 2: Build the Application

```bash
# Navigate to project directory
cd /path/to/CompTestCRM33

# Build with Maven
mvn clean package -DskipTests

# Verify JAR file
ls -lh target/crm-0.0.1-SNAPSHOT.jar
```

## Step 3: Deploy to Azure Container Apps

### 3.1 Create Container Apps Environment

```bash
CONTAINERAPPS_ENV="crm-env"

# Create Container Apps environment
az containerapp env create \
  --name $CONTAINERAPPS_ENV \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION
```

### 3.2 Create Container App with Managed Identity

```bash
CONTAINER_APP="crm-app"

# Create container app
az containerapp create \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --environment $CONTAINERAPPS_ENV \
  --image mcr.microsoft.com/azuredocs/containerapps-helloworld:latest \
  --target-port 8080 \
  --ingress external \
  --cpu 1.0 \
  --memory 2.0Gi \
  --min-replicas 1 \
  --max-replicas 5 \
  --system-assigned

# Get managed identity principal ID
PRINCIPAL_ID=$(az containerapp show \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --query identity.principalId -o tsv)
```

### 3.3 Grant Permissions to Managed Identity

```bash
# Grant Storage Blob Data Contributor role
az role assignment create \
  --assignee $PRINCIPAL_ID \
  --role "Storage Blob Data Contributor" \
  --scope "/subscriptions/$(az account show --query id -o tsv)/resourceGroups/$RESOURCE_GROUP/providers/Microsoft.Storage/storageAccounts/$STORAGE_ACCOUNT"

# Grant Key Vault Secrets User role
az role assignment create \
  --assignee $PRINCIPAL_ID \
  --role "Key Vault Secrets User" \
  --scope "/subscriptions/$(az account show --query id -o tsv)/resourceGroups/$RESOURCE_GROUP/providers/Microsoft.KeyVault/vaults/$KEY_VAULT"
```

### 3.4 Configure Environment Variables

```bash
# Get storage account endpoint
STORAGE_ENDPOINT="https://${STORAGE_ACCOUNT}.blob.core.windows.net"

# Get database connection string
DB_URL="jdbc:mysql://${DB_SERVER}.mysql.database.azure.com:3306/${DB_NAME}?useSSL=true&requireSSL=true"

# Update container app with environment variables
az containerapp update \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --set-env-vars \
    "AZURE_STORAGE_ENABLED=true" \
    "AZURE_STORAGE_ACCOUNT_NAME=$STORAGE_ACCOUNT" \
    "AZURE_STORAGE_CONTAINER_NAME=crm-files" \
    "AZURE_STORAGE_ENDPOINT=$STORAGE_ENDPOINT" \
    "DB_URL=$DB_URL" \
    "DB_USERNAME=$DB_ADMIN" \
    "DB_PASSWORD=secretref:db-password" \
    "APP_TIMEZONE=UTC" \
    "APP_DISPLAY_TIMEZONE=UTC" \
    "APPLICATIONINSIGHTS_CONNECTION_STRING=InstrumentationKey=$INSTRUMENTATION_KEY"
```

### 3.5 Deploy Application JAR

```bash
# Create container registry (if not exists)
ACR_NAME="crmappregistry$(date +%s)"

az acr create \
  --resource-group $RESOURCE_GROUP \
  --name $ACR_NAME \
  --sku Basic \
  --admin-enabled true

# Build and push Docker image
az acr build \
  --registry $ACR_NAME \
  --image crm-app:latest \
  --file Dockerfile \
  .

# Update container app with new image
az containerapp update \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --image "${ACR_NAME}.azurecr.io/crm-app:latest"
```

## Step 4: Alternative - Deploy to Azure App Service

### 4.1 Create App Service Plan

```bash
APP_SERVICE_PLAN="crm-app-plan"

az appservice plan create \
  --name $APP_SERVICE_PLAN \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --sku B2 \
  --is-linux
```

### 4.2 Create Web App

```bash
WEB_APP="crm-webapp-$(date +%s)"

az webapp create \
  --resource-group $RESOURCE_GROUP \
  --plan $APP_SERVICE_PLAN \
  --name $WEB_APP \
  --runtime "JAVA:8-jre8"

# Enable managed identity
az webapp identity assign \
  --name $WEB_APP \
  --resource-group $RESOURCE_GROUP
```

### 4.3 Configure Application Settings

```bash
az webapp config appsettings set \
  --name $WEB_APP \
  --resource-group $RESOURCE_GROUP \
  --settings \
    AZURE_STORAGE_ENABLED=true \
    AZURE_STORAGE_ACCOUNT_NAME=$STORAGE_ACCOUNT \
    AZURE_STORAGE_CONTAINER_NAME=crm-files \
    DB_URL="$DB_URL" \
    DB_USERNAME=$DB_ADMIN \
    DB_PASSWORD="@Microsoft.KeyVault(SecretUri=https://${KEY_VAULT}.vault.azure.net/secrets/db-password/)" \
    APP_TIMEZONE=UTC \
    APPLICATIONINSIGHTS_CONNECTION_STRING="InstrumentationKey=$INSTRUMENTATION_KEY"
```

### 4.4 Deploy JAR File

```bash
az webapp deploy \
  --resource-group $RESOURCE_GROUP \
  --name $WEB_APP \
  --src-path target/crm-0.0.1-SNAPSHOT.jar \
  --type jar
```

## Step 5: Configure Scheduled Tasks with Azure Logic Apps

### 5.1 Create Logic App for Scheduled Tasks

```bash
LOGIC_APP="crm-scheduler"

# Create Logic App
az logic workflow create \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --name $LOGIC_APP \
  --definition '{
    "definition": {
      "$schema": "https://schema.management.azure.com/providers/Microsoft.Logic/schemas/2016-06-01/workflowdefinition.json#",
      "triggers": {
        "Recurrence": {
          "type": "Recurrence",
          "recurrence": {
            "frequency": "Day",
            "interval": 1,
            "timeZone": "UTC",
            "startTime": "2024-01-01T00:00:00Z"
          }
        }
      },
      "actions": {
        "HTTP": {
          "type": "Http",
          "inputs": {
            "method": "POST",
            "uri": "https://'"$CONTAINER_APP"'.azurecontainerapps.io/api/scheduled-task"
          }
        }
      }
    }
  }'
```

## Step 6: Monitoring and Logging

### 6.1 View Application Logs

```bash
# Container Apps logs
az containerapp logs show \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --follow

# App Service logs
az webapp log tail \
  --name $WEB_APP \
  --resource-group $RESOURCE_GROUP
```

### 6.2 Query Application Insights

```bash
# Get Application Insights App ID
APP_ID=$(az monitor app-insights component show \
  --app $APP_INSIGHTS \
  --resource-group $RESOURCE_GROUP \
  --query appId -o tsv)

# Query logs
az monitor app-insights query \
  --app $APP_ID \
  --analytics-query "traces | where message contains 'Azure Blob Storage' | take 10"
```

## Step 7: Verify Deployment

### 7.1 Health Check

```bash
# Get application URL
APP_URL=$(az containerapp show \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --query properties.configuration.ingress.fqdn -o tsv)

# Test health endpoint
curl https://$APP_URL/appinfo/health
```

### 7.2 Test File Operations

```bash
# Test PDF generation
curl -X POST https://$APP_URL/pdf-generator \
  -H "Content-Type: application/json" \
  -d '{"name":"test.pdf","content":"Test content"}'

# Verify blob storage
az storage blob list \
  --container-name crm-files \
  --account-name $STORAGE_ACCOUNT \
  --auth-mode login
```

## Step 8: Configure Auto-Scaling

### 8.1 Container Apps Scaling Rules

```bash
az containerapp update \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --min-replicas 1 \
  --max-replicas 10 \
  --scale-rule-name http-rule \
  --scale-rule-type http \
  --scale-rule-http-concurrency 50
```

### 8.2 App Service Auto-Scaling

```bash
az monitor autoscale create \
  --resource-group $RESOURCE_GROUP \
  --resource $WEB_APP \
  --resource-type Microsoft.Web/sites \
  --name autoscale-rule \
  --min-count 1 \
  --max-count 5 \
  --count 1

az monitor autoscale rule create \
  --resource-group $RESOURCE_GROUP \
  --autoscale-name autoscale-rule \
  --condition "CpuPercentage > 70 avg 5m" \
  --scale out 1
```

## Troubleshooting

### Common Issues

#### Issue: Application fails to start
**Solution**: Check logs for errors
```bash
az containerapp logs show --name $CONTAINER_APP --resource-group $RESOURCE_GROUP
```

#### Issue: Cannot connect to database
**Solution**: Verify firewall rules and connection string
```bash
az mysql flexible-server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --name $DB_SERVER \
  --rule-name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

#### Issue: Blob storage access denied
**Solution**: Verify managed identity permissions
```bash
az role assignment list \
  --assignee $PRINCIPAL_ID \
  --scope "/subscriptions/$(az account show --query id -o tsv)/resourceGroups/$RESOURCE_GROUP"
```

## Cleanup

To remove all resources:

```bash
az group delete --name $RESOURCE_GROUP --yes --no-wait
```

## Next Steps

1. Configure custom domain and SSL certificate
2. Set up Azure Front Door for global distribution
3. Configure backup and disaster recovery
4. Implement CI/CD pipeline with Azure DevOps or GitHub Actions
5. Set up monitoring alerts and dashboards
6. Configure Azure API Management for API gateway

## Support

For issues or questions:
- Review Application Insights logs
- Check Azure service health status
- Consult Azure documentation: https://docs.microsoft.com/azure
