insert into public.tb_role (id, authority) values (1, 'USER') on conflict do nothing;
insert into public.tb_role (id, authority) values (2, 'ADMIN') on conflict do nothing;