@echo off

:: Calling batchfile needs to define variable "batchfile" which is the name of the executing batch
::	i.e.: set batchfile=%~n0

:: Sets up variables for use in calling batches
::   Date variables
::	DATE_DOW = day of week
::	DATE_MM = 2 digit month
::	DATE_DD = 2 digit day of month
::	DATE_YYYY = 4 digit year
::	DATE_YY = 2 digit year
::   Time variables
::	TIME_HH = 2 digit hour
::	TIME_MM = 2 digit minutes
::	TIME_AMPM = AM/PM
::   Log file variables
::	logsuccess = Log file for success messages
::	logerror = Log file for error messages
::	loginfo = Log file for info log messages

:: Initialize date part variables
@For /F "tokens=1-4 delims=/.- " %%A in ('date /T') do (
   set DATE_DOW=%%A
   set DATE_MM=%%B
   set DATE_DD=%%C
   set DATE_YYYY=%%D
   set /a PREV2DATE_YYYY=%%D-2
   set /a PREVDATE_YYYY=%%D-1
)
set DATE_YY=%DATE:~12,2%

:: Initialize time part variables
@For /F "tokens=1,2,3 delims=: " %%A in ('Time /t') do @ (
   set TIME_HH=%%A
   set TIME_MM=%%B
   set TIME_AMPM=%%C
)

:: Initialize log file variables
set logdirectory=D:\app\infa\batch\log\
set logfilestamp=%DATE_YYYY%-%DATE_MM%-%DATE_DD%-%TIME_HH%-%TIME_MM%-%TIME_AMPM%
set logsuccess=%logdirectory%\%batchfile%.success.%logfilestamp%.txt
set logerror=%logdirectory%\%batchfile%.error.%logfilestamp%.txt
set loginfo=%logdirectory%\%batchfile%.info.%logfilestamp%.txt

