alter table Money_Transaction
RENAME COLUMN balance TO amount;

alter table Money_Transaction
ADD COLUMN type int4;