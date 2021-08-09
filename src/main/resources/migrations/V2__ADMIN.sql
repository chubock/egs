insert into users (uid, version, account_expired, account_locked, credential_expired, enabled, first_name,
                   last_name, password, role, username, create_date, last_update_date)
values (substring(md5(random()::text), 0, 33)::uuid, 0, false, false, false, true, 'admin', 'lastname',
        '$2a$12$r8YBeofGplO30GNHVtayxuV6ctkZ6BwAIGeX4E/CWvD4BzHUXywWO', 0, 'admin', now(), now());
