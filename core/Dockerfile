FROM openjdk:8
RUN mkdir /app
COPY target/universal/remix-core-0.1.0.zip /
COPY remix.cfg /etc/
RUN unzip /remix-core-0.1.0.zip
RUN ln -s /remix-core-0.1.0/bin/remix-core /bin/remix-core
RUN echo play.http.secret.key=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c50) >> /remix-core-0.1.0/conf/application.conf