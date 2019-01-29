select
	lc1.MemberKey,
	Patient_FirstName,
	Patient_LastName,
	Patient_PhoneNumber,
	lc1.Activation_Date,
	trantype
from (
	select 
	memberkey,
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
	group by SubmittedFirstName, Submittedlastname, SubmittedPatientPhone, Processdate, trantype, memberkey
) lc1
join (
	select 
		memberkey,
		Processdate as Activation_Date,
		sum(net_activation_qty) as net_activation_qty
	from sc_LakerClaim lc (nolock)
	where lc.BIN= '610739'
	and PCN='JT1'
	and GROUPNUMBER = '915691'
	group by Processdate, memberkey)lc2
on lc1.memberkey = lc2.memberkey
where lc1.net_activation_qty = 1
and lc2.net_activation_qty = 1
and lc1.Activation_Date >= dateadd(week,-1,convert(date,getdate()))
and lc2.Activation_Date < dateadd(week,-1,convert(date,getdate()))
order by lc1.Activation_Date desc
