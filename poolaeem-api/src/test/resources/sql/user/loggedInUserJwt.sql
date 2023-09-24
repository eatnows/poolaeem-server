delete from logged_in_user_jwt;

insert into `logged_in_user_jwt` (id, user_id, token, client_ip, user_agent, issued_at, expires_at,
                                  created_at)
values ('jwt-1', 'user-1',
        'eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IlJlZnJlc2giLCJjb2RlIjoidXNlci1pZCIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3NjEzOTIzLCJleHAiOjM2ODg5MDk5MjN9.abGtDWHblKjihChHShvBKcKo1uu0CkLPInmDduFNPeYMUduZICgvaYzBlvvTK8brekrSC3ahlH4ROnCTxVwFN-IqxUxdIxNS-FXAQcoZBWRnWRHeLK56ZcwKo1RmCbzD4d1hEP907fb_zjAPrgrk6GgWO-L-Yq0z0K1Ntgdgz5M7I4gWRayeI13TBdaR6DqcAfrNCqK3Gg_0cxtgwN7cGvVBVPoG1jufXPt1uxynwfMHzTXY7UtvKRjDO3g0vyTBF52abK85eBMmvKIaGMEKOKWuN75ZYFS8xIFluGOouZsoLII_jTVIV1xMcWHe0a3WNxV5sh-EugLrVsJVfIJofQ',
        '', '', DATE_SUB(now(), interval 1 DAY), DATE_ADD(now(), interval 1 DAY), DATE_SUB(now(), interval 1 DAY))
;
