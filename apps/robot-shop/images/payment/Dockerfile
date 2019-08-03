FROM composer AS build

COPY composer.json /app/

RUN composer install

FROM php:7.3-apache

RUN docker-php-ext-install pdo_mysql

COPY status.conf /etc/apache2/mods-available/status.conf

RUN a2enmod rewrite && a2enmod status

COPY ports.conf /etc/apache2/ports.conf

WORKDIR /var/www/html

COPY --from=build /app/vendor/ /var/www/html/vendor/

COPY html/ /var/www/html

