truncate table `user`;

-- test@poolaeem.com  / S9FZLY3MHeq810Awrkfv6BLNMsItUrlX8Yt0jbQ3AR8ajOnyvo97PY5CoMUz6PyGwQ==
insert into `user` (id, email, name, role, oauth_provider, oauth_id, profile_image, terms_version, is_deleted, last_logged_at, updated_by,
                    created_at, updated_at)
values ('user-1', 'S9FZLY3MHeq810Awrkfv6BLNMsItUrlX8Yt0jbQ3AR8ajOnyvo97PY5CoMUz6PyGwQ==', '풀내임', 'ROLE_USER', 'google', '123457890', 'file-1', 'V1', false, '2023-06-25 00:52:17.123', 'user-1',
        '2023-06-25 00:52:17.123', '2023-06-25 00:52:17.123'),
       ('user-2', 'S9FZLY3MHeq810Awrkfv6BLNMsItUrlX8Yt0jbQ3AR8ajOnyvo97PY5CoMUz6PyGwQ==', '삭제 풀내임', 'ROLE_USER', 'google', '123457890', 'file-2', 'V1', true, '2023-06-25 00:52:18.123', 'user-2',
        '2023-06-25 00:52:18.123', '2023-06-25 00:52:18.123')
;


