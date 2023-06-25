truncate table `user`;

insert into `user` (id, email, name, oauth_provider, oauth_id, profile_image, terms_version, is_deleted, updated_by,
                    created_at, updated_at)
values ('user-1', 'test@poolaeem.com', '풀내임', 'google', '123457890', null, 'V1', false, 'user-1',
        '2023-06-25 00:52:17.123', '2023-06-25 00:52:17.123')
;


