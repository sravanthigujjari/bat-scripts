select
	Patient_FirstName,
	Patient_LastName,
	Patient_PhoneNumber,
	Activation_Date,
	trantype
from(
	select 
		SubmittedFirstName as Patient_FirstName,
		Submittedlastname as Patient_LastName,
		SubmittedPatientPhone as Patient_PhoneNumber,
		Processdate as Activation_Date,
		trantype,
		sum(net_activation_qty) as net_activation_qty
	from sc_LakerClaim lc (nolock)
	where lc.BIN= '610739'
	and PCN='JT1'
	and GROUPNUMBER = '915691'
	group by SubmittedFirstName, Submittedlastname, SubmittedPatientPhone, Processdate, trantype
) lc1
where net_activation_qty = 1
and Activation_Date >= dateadd(week,-1,convert(date,getdate()))
and memberKey not in (
	-- this sub query may have problem of including reversed claims
	select memberKey 
	from sc_LakerClaim 
	where Activation_Date < dateadd(week,-1,convert(date,getdate())))	
order by Activation_Date desc
