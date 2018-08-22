@echo off

set batchfile=%~n0
call D:\app\infa\batch\initialize.bat

set processfile=D:\data\SalesRepKit\YYYYMMDD_OUT_SC_SalesRepKitFulfillment.csv
set renamed=%DATE_YYYY%%DATE_MM%%DATE_DD%_OUT_SC_SalesRepKitFulfillment.csv
set archive=D:\data\SalesRepKit\Archive
REM set dest=\\prodftp\FTPData\PartnerData\SingleCare\SalesRepKit
set PSFTP_FILE=D:\app\infa\batch\FTPScripts\PSFTP_sc_Export_SalesRepKitFulfillment.txt

if exist %processfile% (
	echo Found process file, attempting to rename >> %loginfo%
	rename %processfile% %renamed% >> %loginfo%
) else (
	goto file_not_found
)

if exist D:\data\SalesRepKit\%renamed% (
	REM copy D:\data\SalesRepKit\%renamed% %dest% /Y
	if exist %PSFTP_FILE% del %PSFTP_FILE%
	echo cd /PartnerData/SingleCare/SalesRepKit >> %PSFTP_FILE%
	echo put D:\data\SalesRepKit\%renamed% >> %PSFTP_FILE%
	psftp ftp.scriptrelief.com -l informaticauser -pw MS#@8AKqjSD2$t7z -P 22 -b %PSFTP_FILE% -be >> %loginfo%
	move /y D:\data\SalesRepKit\%renamed% %archive%
	goto got_file
) else (
	goto file_not_found
)

:got_file
echo Sent The Files %DATE% %TIME% > %logsuccess%
echo Sent The Files %DATE% %TIME% 
goto done

:file_not_found
echo Could Not Find Files. %DATE% %TIME% > %logerror%
echo Could not find files.
REM exit 99

:done




-----

pick up file from ftp server:


username: informaticauser
Password: MS#@8AKqjSD2$t7z for that FTP user
server name: ftp.scriptrelief.com 
port 22


file name: sc_salesrepsegment.csv


place the above file to this folder:
D:\data\B2B2C_Actions

rename: sc_salesrepsegment_current_date

archive:
archive renamed file to below folder
D:\data\B2B2C_Actions\archive

