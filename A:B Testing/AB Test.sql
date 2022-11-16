drop table if exists public.application

create table public.application
(applicant_id int,
channel char(50),
exp_group char(20),
city char(20),
event char(50),
event_date date
);

copy public.application
from '/Users/zack/Desktop/application.csv' delimiters ',' CSV;

select *
from public.application;

select applicant_id, exp_group, city, event_date
from public.application 
where event = 'first_batch_completed_date'
and exp_group = 'control';

select distinct event
from public.application;

-- Control group

select count(exp_group) as "Control1"
from public.application 
where exp_group = 'control'
and event = 'application_date';

select count(exp_group) as "Control2"
from public.application 
where exp_group = 'control'
and event = 'card_mailed_date';

select count(exp_group) as "Control3"
from public.application 
where exp_group = 'control'
and event = 'card_activation_date';

select count(exp_group) as "Control4"
from public.application 
where exp_group = 'control'
and event = 'background_check_initiated_date';

select count(exp_group) as "Control5"
from public.application 
where exp_group = 'control'
and event = 'background_check_completed_date';

select count(exp_group) as "Control6"
from public.application 
where exp_group = 'control'
and event = 'orientation_completed_date';

select count(exp_group) as "Control7"
from public.application 
where exp_group = 'control'
and event = 'first_batch_completed_date';



-- Treatment group

select count(exp_group) as "Treatment1"
from public.application 
where exp_group = 'treatment'
and event = 'application_date';

select count(exp_group) as "Treatment2"
from public.application 
where exp_group = 'treatment'
and event = 'background_check_initiated_date';

select count(exp_group) as "Treatment3"
from public.application 
where exp_group = 'treatment'
and event = 'card_mailed_date';

select count(exp_group) as "Treatment4"
from public.application 
where exp_group = 'treatment'
and event = 'background_check_completed_date';

select count(exp_group) as "Treatment5"
from public.application 
where exp_group = 'treatment'
and event = 'card_activation_date';

select count(exp_group) as "Treatment6"
from public.application 
where exp_group = 'treatment'
and event = 'orientation_completed_date';

select count(exp_group) as "Treatment7"
from public.application 
where exp_group = 'treatment'
and event = 'first_batch_completed_date';

