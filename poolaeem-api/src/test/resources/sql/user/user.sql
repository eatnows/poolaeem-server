truncate table `user`;

-- test@poolaeem.com  / S9FZLY3MHeq810Awrkfv6BLNMsItUrlX8Yt0jbQ3AR8ajOnyvo97PY5CoMUz6PyGwQ==
insert into `user` (id, email, name, role, oauth_provider, oauth_id, profile_image, terms_version, is_deleted, updated_by,
                    created_at, updated_at)
values ('user-1', 'S9FZLY3MHeq810Awrkfv6BLNMsItUrlX8Yt0jbQ3AR8ajOnyvo97PY5CoMUz6PyGwQ==', '풀내임', 'ROLE_USER', 'google', '123457890', null, 'V1', false, 'user-1',
        '2023-06-25 00:52:17.123', '2023-06-25 00:52:17.123')
;


