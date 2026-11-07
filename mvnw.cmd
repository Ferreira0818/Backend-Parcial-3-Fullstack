@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@IF "%DEBUG%"=="" @ECHO OFF
@REM set %ENABLE_DELAYED_EXPANSION% to on if you need it
@SETLOCAL ENABLEDELAYEDEXPANSION

@SET MAVEN_CMD_LINE_ARGS=%*

@SET MAVEN_PROJECTBASEDIR=%~dp0
@IF NOT "%MAVEN_PROJECTBASEDIR:~-1%"=="\" SET MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%\

@IF EXIST "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties" (
    @FOR /F "tokens=2 delims==" %%G IN ('FINDSTR "distributionUrl" "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties"') DO @SET MAVEN_DISTRIBUTION_URL=%%G
)

@IF "%MAVEN_DISTRIBUTION_URL%"=="" (
    @SET MAVEN_DISTRIBUTION_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip
)

@FOR %%i IN ("%MAVEN_DISTRIBUTION_URL%") DO @SET MAVEN_ZIP=%%~nxi
@FOR /F "tokens=1,2,3,4 delims=-" %%A IN ("%MAVEN_ZIP%") DO @SET MAVEN_DIR_NAME=apache-maven-%%C

@SET WRAPPER_DIR=%USERPROFILE%\.m2\wrapper\dists\%MAVEN_DIR_NAME%
@SET MAVEN_HOME=%WRAPPER_DIR%\%MAVEN_DIR_NAME%

@IF NOT EXIST "%MAVEN_HOME%\bin\mvn.cmd" (
    @ECHO Descargando Maven...
    @IF NOT EXIST "%WRAPPER_DIR%" @MKDIR "%WRAPPER_DIR%"
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('%MAVEN_DISTRIBUTION_URL%', '%WRAPPER_DIR%\%MAVEN_ZIP%'); Expand-Archive -Path '%WRAPPER_DIR%\%MAVEN_ZIP%' -DestinationPath '%WRAPPER_DIR%' -Force"
)

@CALL "%MAVEN_HOME%\bin\mvn.cmd" %MAVEN_CMD_LINE_ARGS%
@EXIT /B %ERRORLEVEL%
