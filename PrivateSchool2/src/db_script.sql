CREATE DATABASE IF NOT EXISTS `privateschool2` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `privateschool2`;

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('headmaster','trainer','student') NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varbinary(512) NOT NULL,
  `key` varbinary(512) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `courses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `stream` enum('java','csharp') NOT NULL,
  `type` enum('part','full') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `assignments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `courseid` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `submissiondate` date NOT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`courseid`,`title`,`submissiondate`),
  CONSTRAINT `FK_assignments_courses` FOREIGN KEY (`courseid`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `students` (
  `userid` int(11) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `birthdate` date NOT NULL,
  `tuitionfees` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`userid`),
  CONSTRAINT `FK_students_users` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `trainers` (
  `userid` int(11) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  PRIMARY KEY (`userid`),
  CONSTRAINT `FK_trainers_users` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `courses_students` (
  `courseid` int(11) NOT NULL,
  `studentid` int(11) NOT NULL,
  PRIMARY KEY (`courseid`,`studentid`),
  KEY `FK_courses_students_students` (`studentid`),
  CONSTRAINT `FK_courses_students_courses` FOREIGN KEY (`courseid`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_courses_students_students` FOREIGN KEY (`studentid`) REFERENCES `students` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `courses_trainers` (
  `courseid` int(11) NOT NULL,
  `trainerid` int(11) NOT NULL,
  PRIMARY KEY (`courseid`,`trainerid`),
  KEY `FK_courses_trainers_trainers` (`trainerid`),
  CONSTRAINT `FK_courses_trainers_courses` FOREIGN KEY (`courseid`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_courses_trainers_trainers` FOREIGN KEY (`trainerid`) REFERENCES `trainers` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `assignments_students` (
  `assignmentid` int(11) NOT NULL,
  `studentid` int(11) NOT NULL,
  `submitteddate` datetime DEFAULT NULL,
  `oralpoints` int(11) DEFAULT NULL,
  `writingpoints` int(11) DEFAULT NULL,
  PRIMARY KEY (`assignmentid`,`studentid`),
  KEY `FK_assignments_students_students` (`studentid`),
  CONSTRAINT `FK_assignments_students_assignments` FOREIGN KEY (`assignmentid`) REFERENCES `assignments` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_assignments_students_students` FOREIGN KEY (`studentid`) REFERENCES `students` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `courseid` int(11) NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_schedule_courses` (`courseid`),
  CONSTRAINT `FK_schedule_courses` FOREIGN KEY (`courseid`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
