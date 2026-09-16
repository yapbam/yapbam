; NSIS installer script for Yapbam
; The PROJECT_VERSION variable is auto-injected by nsis-maven-plugin
; via the generated header file (injectHeaderFile=true by default).

!define PRODUCT_NAME "Yapbam"

Name "${PRODUCT_NAME} ${PROJECT_VERSION}"
InstallDir "$EXEDIR\Yapbam"
InstallDirRegKey HKCU "Software\${PRODUCT_NAME}" ""
Icon "icons\yapbam.ico"

RequestExecutionLevel user

Page directory
Page instfiles

Section "${PRODUCT_NAME}"
  SetOutPath "$INSTDIR"
  File "Yapbam.exe"
  File "yapbam.sh"
  SetOutPath "$INSTDIR\App"
  File "target\program.jar"
SectionEnd
