@echo off

set firstArgument=%1
GOTO Begin:

:Begin
echo First argument is: %firstArgument%
IF "%firstArgument%"=="UNIQUE_FILE_EXTENSIONS" GOTO UniqueFileExtensions:
IF "%firstArgument%"=="FILTER_FINAL_DUMP" GOTO FilterFinalDump:
IF "%firstArgument%"=="FILES_CHANGED_TOGETHER" GOTO FilesChangedTogether:
IF "%firstArgument%"=="TEST" GOTO Test:
GOTO Exit:

:UniqueFileExtensions
git_data_collector.sh -fe
GOTO Exit:

:FilterFinalDump
set RESTVAR=
shift
:loop1
if "%1"=="" goto after_loop
set RESTVAR=%RESTVAR% %1
shift
goto loop1

:after_loop
git_data_collector.sh -filter %RESTVAR%
GOTO Exit:

:FilesChangedTogether
git_data_collector.sh -fct
GOTO Exit:

:TEST
echo Test output
GOTO Exit:

:Exit
exit