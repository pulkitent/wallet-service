alter table transaction add COLUMN created_at TIMESTAMP DEFAULT NOW();
alter table transaction ADD COLUMN remark varchar(255);