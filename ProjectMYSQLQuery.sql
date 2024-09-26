# project라는 유저 생성
CREATE USER 'project'@'localhost' IDENTIFIED BY 'qwer1234!';

# database project 삭제 및 생성 
DROP DATABASE project;
# database 기본 문자 셋 utf8으로 설
CREATE DATABASE project DEFAULT CHARACTER SET UTF8;
#프로젝트를 데이터베이스로 사용
USE project;
# project유저가 project데이터베이스에서 사용할 모든 권한 부여
GRANT ALL PRIVILEGES ON project.* TO 'project'@'localhost';



# 유저(소비자) 테이블
DROP TABLE user;
CREATE table user (
	idx BIGINT(10) primary key AUTO_INCREMENT,
	username VARCHAR(50) UNIQUE KEY not null,
	password varchar(100) not null,
	name varchar(50) not null,
	email varchar(50),
	postcode varchar(10),
  	address varchar(200),
  	detailAddress varchar(200),
  	regidate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  	authority varchar(20) default 'ROLE_USER',
  	enabled TINYINT(1) default 1,
  	provider varchar(20) default 'LOCAL',
  	providerId varchar(100),
  	isLocked TINYINT(1) default 0,
  	failCount INT default 0,
  	lockTimes TIMESTAMP
);

SELECT * FROM user;

# 셀러(판매자) 테이블
DROP TABLE seller;
CREATE table seller (
	idx BIGINT(10) primary key AUTO_INCREMENT,
	businessNum varchar(100) UNIQUE KEY NOT NULL,
	username VARCHAR(50) UNIQUE KEY NOT NULL,
	password varchar(100) not null,
	name varchar(50) not null,
	businessName varchar(100) UNIQUE KEY not null,
	email varchar(50),
	postcode varchar(10),
  	address varchar(200),
  	detailAddress varchar(200),
  	regidate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  	authority varchar(20) default 'ROLE_SELLER',
  	enabled TINYINT(1) default 1,
  	isLocked TINYINT(1) default 0,
  	failCount INT default 0,
  	lockTimes TIMESTAMP
);

select * from seller;

# 관리자 테이블 생성
DROP TABLE admin;
CREATE table admin (
	idx INT(10) primary key AUTO_INCREMENT,
	username varchar(50) UNIQUE KEY  not null,
	password varchar(100) not null,
	email varchar(100),
	authority varchar(20) default 'ROLE_ADMIN'
);
# 테이블에 초기 관리자 넣기
INSERT into admin (idx, username, password, email, authority)
	values (1, 'admin', 'qwer1234!', '', 'ROLE_ADMIN');
	
# 관리자 테이블 확인
SELECT * FROM admin;


######## 제품 테이블 ########

CREATE TABLE product (
	idx bigint(10) PRIMARY KEY AUTO_INCREMENT,
	productNum varchar(10) not null,
	businessName varchar(100) not null,
	productName varchar(50) UNIQUE KEY not null,
	quantity int(10) not null,
	cost bigint(100),
	FOREIGN KEY (businessName) REFERENCES seller (businessName)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

# 예약 테이블 생성 
DROP TABLE reservation;
CREATE TABLE reservation (
	idx BIGINT(10) PRIMARY KEY AUTO_INCREMENT,
	reserveNum varchar(50) UNIQUE KEY  not null,
	reserveDate date UNIQUE KEY  not null,
	reserveTime time UNIQUE KEY  not null,
	username VARCHAR(50) not null,
	isPaid int(1) not null,
	costAll bigint(10) UNIQUE KEY NOT NULL,
	businessName varchar(100) not null,
	productName varchar(30) not null,
	FOREIGN KEY (businessName) REFERENCES seller (businessName),
	FOREIGN KEY (productName) REFERENCES product (productName)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);


######## 결제 ########

CREATE TABLE transaction (
	idx bigint(10) PRIMARY KEY AUTO_INCREMENT,
	reserveNum int not null,
	costAll BIGINT(100),
	methodOfPayment varchar(30) not null,
	transactionDate Date not null,
	FOREIGN KEY (costAll) REFERENCES reservation (costAll)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

######## 커뮤니티 게시판 테이블 ########
DROP TABLE community;
CREATE TABLE community (
	idx BIGINT(10) PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(50) not null,
	title varchar(50) not null,
	content varchar(1000) not null,
  	postdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  	viewCount int(10) DEFAULT 0 NOT NULL,
  	ofile varchar(200),
  	sfile varchar(100),
  	FOREIGN KEY (username) REFERENCES user (username)
  	ON DELETE CASCADE
	ON UPDATE CASCADE
);

# 커뮤니티 댓글 테이블
DROP TABLE commuComment;
create table commuComment (
    cIdx int(10) primary key, -- 댓글 인덱스 번호
    writer VARCHAR(50), -- 댓글 작성자 아이디
    commentText VARCHAR(500), -- 댓글 내용
    refGroup BIGINT(6), -- 원글 번호
    deleted INT(1) default 0, -- 지워진 댓글 여부 1:yes 0 no
    postdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (refGroup) REFERENCES community(idx),
    FOREIGN KEY (writer) REFERENCES user(username)
    ON DELETE CASCADE
	ON UPDATE CASCADE
);

# 커뮤니티 게시판 좋아요 테이블
CREATE TABLE commuLike (
	likeCount INT(10) default 0,
	username VARCHAR(50) NOT NULL,
	boardIdx BIGINT(10) NOT NULL,
	FOREIGN KEY (boardIdx) REFERENCES community (idx),
	FOREIGN KEY (username) REFERENCES user (username)
    ON DELETE CASCADE
	ON UPDATE CASCADE
);


######## 공지사항 게시판 테이블 ########
DROP TABLE announcement;
CREATE TABLE announcement (
	idx bigint(10) PRIMARY KEY AUTO_INCREMENT,
	username varchar(100) not null,
	title varchar(50) not null,
	content varchar(1000) not null,
  	postdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  	viewCount int(10) DEFAULT 0 NOT NULL,
  	ofile varchar(200),
  	sfile varchar(100),
  	FOREIGN KEY (username) REFERENCES admin (username)
  	ON DELETE CASCADE
	ON UPDATE CASCADE
);

######## 1대1 문의 게시판 ##########
DROP TABLE inquiry;
CREATE TABLE inquiry (
	idx bigint(10) PRIMARY KEY AUTO_INCREMENT,
	parentIdx int(10) default 0,
	username varchar(100) not null,
 	parentUsername varchar(100),
 	title varchar(50) not null,
	content varchar(1000) not null,
	postdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	viewCount int(10) DEFAULT 0 NOT NULL,
	responses int(10) DEFAULT 0 NOT NULL,
  	ofile varchar(200),
  	sfile varchar(100),
  	inquiryPassword varchar(20) not null,
  	FOREIGN KEY (username) REFERENCES user (username),
  	FOREIGN KEY (username) REFERENCES admin (username)
  	ON DELETE CASCADE
	ON UPDATE CASCADE
);
