@echo off

set firstArgument=%1
GOTO Begin:

:Begin
echo First argument is: %firstArgument%
IF "%firstArgument%"=="LOG_NO_MERGES" GOTO LogNoMerges:
IF "%firstArgument%"=="LOG" GOTO Log:
IF "%firstArgument%"=="UNIQUE_FILE_EXTENSIONS" GOTO UniqueFileExtensions:
IF "%firstArgument%"=="FILTER_FINAL_DUMP" GOTO FilterFinalDump:
IF "%firstArgument%"=="FILES_CHANGED_TOGETHER" GOTO FilesChangedTogether:
IF "%firstArgument%"=="LOG_DATE_RESTRICTED_NO_MERGES" GOTO LogDateRestrictedNoMerges:
IF "%firstArgument%"=="LOG_DATE_RESTRICTED" GOTO LogDateRestricted:
IF "%firstArgument%"=="TEST" GOTO Test:
GOTO Exit:

:LogNoMerges
git_data_collector.sh --no-merges -log
GOTO Exit:

:Log
git_data_collector.sh -log
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
REM echo %RESTVAR%
GOTO Exit:

:FilesChangedTogether
git_data_collector.sh -fct
GOTO Exit:

:LogDateRestrictedNoMerges
set argC=0
for %%x in (%*) do Set /A argC+=1
echo %argC%
if %argC% gtr 3 GOTO MultiVariableDate:
GOTO SingleVariableDate:
:SingleVariableDate
echo "Single"
set RESTVAR=%2="%3"
git_data_collector.sh --no-merges %RESTVAR%
echo %RESTVAR%
GOTO Exit:
:MultiVariableDate
echo "Double"
set RESTVAR=%2="%3" %4="%5"
git_data_collector.sh --no-merges %RESTVAR%
GOTO Exit:

:LogDateRestricted
set argC=0
for %%x in (%*) do Set /A argC+=1
echo %argC%
if %argC% gtr 3 GOTO MultiVariableDate2:
GOTO SingleVariableDate2:
:SingleVariableDate2
echo "Single"
set RESTVAR=%2="%3"
git_data_collector.sh %RESTVAR%
echo %RESTVAR%
GOTO Exit:
:MultiVariableDate2
echo "Double"
set RESTVAR=%2="%3" %4="%5"
git_data_collector.sh --no-merges %RESTVAR%
GOTO Exit:

:TEST
echo Test output
GOTO Exit:

:Exit
exit