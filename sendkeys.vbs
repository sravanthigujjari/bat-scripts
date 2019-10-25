set WshShell = WScript.CreateObject("WScript.Shell")
DO 
WScript.Sleep 60000 
WshShell.sendKeys "{SCROLLLOCK}" 
WScript.Sleep 500
WshShell.sendKeys "{SCROLLLOCK}" 
LOOP 
