delete from `file`;

insert into `file` (`id`, `file_name`, `file_size`, `file_extension`, `path`, `is_deleted`, `created_by`, `updated_by`,
                    `created_at`, `updated_at`)
values ('file-1', '프로필이미지', 8196, 'png', 'images/profile/', false, 'user-1', 'user-1', '2023-07-16 23:18:38.123',
        '2023-07-16 23:18:38.123')
;

