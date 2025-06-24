# Test script for BLPS Lab1 API
Write-Host "Starting API tests..." -ForegroundColor Green

# Wait for application to start
Write-Host "Waiting for application to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 20

# Test 1: Check if application is running
Write-Host "`n1. Testing application availability..." -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/unique?email=test@test.com" -UseBasicParsing
    Write-Host "✓ Application is running" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Application is not responding: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 2: Test user registration
Write-Host "`n2. Testing user registration..." -ForegroundColor Cyan
try {
    $body = @{
        email = "test@test.com"
        password = "password123"
    } | ConvertTo-Json
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" -Method POST -Body $body -ContentType "application/json" -UseBasicParsing
    Write-Host "✓ User registration successful" -ForegroundColor Green
} catch {
    Write-Host "✗ User registration failed: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorContent = $reader.ReadToEnd()
        $reader.Close()
        Write-Host "Error details: $errorContent" -ForegroundColor Red
    }
}

# Test 3: Test BPMN process start
Write-Host "`n3. Testing BPMN process start..." -ForegroundColor Cyan
try {
    $body = @{
        test = "value"
        action = "test"
    } | ConvertTo-Json
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/process/start" -Method POST -Body $body -ContentType "application/json" -UseBasicParsing
    Write-Host "✓ BPMN process start successful" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Gray
} catch {
    Write-Host "✗ BPMN process start failed: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorContent = $reader.ReadToEnd()
        $reader.Close()
        Write-Host "Error details: $errorContent" -ForegroundColor Red
    }
}

# Test 4: Test Camunda Cockpit access
Write-Host "`n4. Testing Camunda Cockpit access..." -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/camunda/app/cockpit/default/" -UseBasicParsing
    Write-Host "✓ Camunda Cockpit is accessible" -ForegroundColor Green
} catch {
    Write-Host "✗ Camunda Cockpit access failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nAll tests completed!" -ForegroundColor Green 