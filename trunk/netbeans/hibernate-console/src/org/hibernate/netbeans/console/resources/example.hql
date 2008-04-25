select obj from Object obj
join obj.relation1 rel1
left outer join rel1.relation2 rel2
right outher join rel2.relation3 rel3
where rel3.id = 100 and rel2.value = 'Some Stuff'


"select a " +
"from Object " +
"obj "
