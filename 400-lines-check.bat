:: replace test-input-file.txt" with real file name

@echo off

setlocal EnableDelayedExpansion
set "cmd2=findstr /R /N "^^" test-input-file.txt | find /C ":""
for /f %%a in ('!cmd2!') do set number=%%a
echo %number%

if %number% GEQ 400 (
	echo "i am greater than 400"
) ELSE (
	echo "i am not greater than 400"
)
