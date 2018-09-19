@echo off

:: Runs AutoMemberQC Proc and emails the results

set batchfile=%~n0
call D:\app\infa\batch\initialize.bat
set path=C:\Program Files (x86)\Febooti Command line email\;c:\windows\system32;%PATH%
set outputfile=D:\memberqcresults_sc.txt
set RENAMED_FILE=MEMBERQC.TXT

rem if exist %outputfile% 
rem del %outputfile%

:: Run Proc
:: and cat output to file D:\memberqcresults.txt

rem echo Executing a_ETL_SP_QC >> %loginfo%
rem sqlcmd.exe -b -w 512 -h-1 -o %outputfile% -S Simpleplace.SingleCare.com -U mercatus3 -P S1ngl3MeNow -d SingleCare /Q "exec a_ETL_SP_QC" >> %loginfo%
rem IF ERRORLEVEL 1 (
    rem echo Error executing a_ETL_SP_QC >> %loginfo%
    rem echo Error executing a_ETL_SP_QC > %logerror%
    rem goto errorproc
rem ) ELSE (
   rem echo Success a_ETL_SP_QC >> %loginfo%
rem )

if exist %outputfile% (
	febootimail -config "D:\app\infa\batch\febooti_config_memberqc_sc.txt" >> %loginfo%
	D:
	cd D:\data\inbound\MemberQC >> %loginfo%
	del %RENAMED_FILE% >> %loginfo%
	echo Completed Successfully > %logsuccess%
	goto done

) else (
    echo Output file not found > %logerror%
    goto error
)


:error
exit 99

:errorproc
exit 111

:done
