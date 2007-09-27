@echo off
set IDL_HOME=C:\projects\mozilla\vc7
set MOZ_HOME=C:\projects\mozilla\mozilla-source-1.6\obj-i586-pc-msvc-JBoss\dist
set PATH=%MOZ_HOME%\bin;%IDL_HOME%\bin
set INCLUDE=
set LIB=
@echo on

@echo PATH=%PATH%

xpidl -m %1 -I %MOZ_HOME%\idl %2

pause
@echo off
@set MOZ_HOME=
@set IDL_HOME=
@echo on