create database analytics_freshmart;
use analytics_freshmart;
create table categories(Category_Id int primary key,Category_Name varchar(80) not null);
-- Products Table
create table Products(Product_Id int primary key,Product_Name varchar(100) not null, Category_Id int,expiry_date_on date,count_of_stock int,price decimal(10,2),supplier_name varchar(100),discount_percent int,foreign key(Category_Id) references categories(Category_Id));
-- Sales Table
create table SalesTransactions(transaction_id int primary key,Product_Id int,quantity int,sale_date date,foreign key (Product_Id) references Products(Product_Id));
insert into categories values
(1001,'Frozen Foods'),
(1002,'Household_Items'),
(1003,'Dairy'),
(1004,'Snacks'),
(1005,'Beauty_Products'),
(1006,'Stationery');
-- Insert Products
insert into Products values
(1,'Fruits',1001,'2026-04-08',120,80,'chills',10),
(2,'Ice Cream',1001,'2026-04-13',60,150,'arun',5),
(3,'Floor cleaner',1002,'2026-10-20',70,180,'Rinn',8),
(4,'Detergent Powder',1002,'2027-05-01',200,280,'Surf excel',10),
(5,'Milk',1003,'2026-04-05',100,30,'Amul',3),
(6,'Cheese',1003,'2026-04-20',100,120,'Amul',5),
(7,'Chocolates',1004,'2026-05-20',180,40,'Cadbury',10),
(8,'Chips',1004,'2026-06-01',180,20,'Lays',5),
(9,'Shampoo',1005,'2026-09-01',110,100,'Loreal',10),
(10,'Perfume',1005,'2026-10-01',50,190,'Bellavita',10),
(11,'Pen',1006,'2027-12-01',160,10,'classmate',1),
(12,'Books',1006,'2027-12-01',190,50,'classmate',3);
-- Insert Sales
insert into SalesTransactions values
(2001,1,10,'2026-04-08'),
(2002,3,20,'2026-03-30'),
(2003,5,15,'2026-04-04');
-- Fetch products that will expire within the next 7 days 
-- and still have high stock levels
select Product_Name,expiry_date_on,count_of_stock
from Products
where expiry_date_on between '2026-04-09' and '2026-04-16'
and count_of_stock>50;
-- 2.DEAD STOCK(No sales in last 60 days)
select p.Product_Name
from Products p
left join SalesTransactions s
on p.Product_Id = s.Product_Id
and s.sale_date >= '2026-02-08'
where s.transaction_id is null;
-- 3. LAST MONTH REVENUE (March 2026)
select c.Category_Name,
sum(p.price*s.quantity) as total_revenue
from SalesTransactions s
join Products p on s.Product_Id=p.Product_Id
join Categories c on p.Category_Id=c.Category_Id
where s.sale_date>='2026-03-01'
and s.sale_date<'2026-04-01'
group by c.Category_Name
order by total_revenue desc;
