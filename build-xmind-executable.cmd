C:\IDEs\Eclipse Toolset\rcp-luna-SR2-win32
mvn clean verify
"C:\IDEs\Eclipse Toolset\rcp-luna-SR2-win32\eclipse.exe" -nosplash -consoleLog -application org.eclipse.equinox.p2.director -repository file:"E:\~SANDBOX\JAVA\xmind-intelliJ\releng\org.xmind.product\target\repository\ -installIU org.xmind.cathy.product -profile XMindProfile -roaming -destination "E:\~SANDBOX\JAVA\xmind-intelliJ\target\xmind" -p2.os win32 -p2.ws win32 -p2.arch x86_64


@REM -p2.arch x86|x86_64