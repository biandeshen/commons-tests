###按时间段查询
SELECT * FROM test where DATE_SUB(NOW(), INTERVAL 3 MONTH) >= date(time);