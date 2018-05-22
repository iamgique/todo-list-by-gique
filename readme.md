# TODO-LIST-BY-GIQUE
- Run on: Java runtime version 1.8 with springboot
- Database: postgresql 
- Deploy: Heroku
- Connect to: https://developers.line.me
- Using: Line message api BOT: https://developers.line.me/en/docs/messaging-api/building-bot/
- Using: Line reply message.
- Using: Line push message.
- Getting Started on Heroku with Java: https://devcenter.heroku.com/articles/getting-started-with-java
- Git: https://git.heroku.com/todo-list-by-gique.git

#### You can add line bot todo list at QR Code below:
![alt text](https://qr-official.line.me/M/admvtCr6GO.png)

## Prerequisite
Make sure you have
- Register Heroku
- Install Java version 8
- Maven 3

You can download Heroku CLI: https://devcenter.heroku.com/articles/getting-started-with-java#set-up

After that, you can create datasource at Heroku or local.

## Data store
- Choose the data stores: https://elements.heroku.com/addons/categories/data-stores
- In this project choose "postgresql".
- You can create data stores manual at below:
```sh
$ heroku addons:create heroku-postgresql:hobby-dev
$ heroku addons
```
- Config and lookup
```sh
$ heroku config
$ heroku pg
$ heroku pg:psql {database-name} --app {app-name}
```

## Install Postgresql on localhost
- Uri for download tools or using CLI: http://postgresapp.com/documentation/cli-tools.html
- After that,
```sh
$ sudo mkdir -p /etc/paths.d &&
echo /Applications/Postgres.app/Contents/Versions/latest/bin | sudo tee /etc/paths.d/postgresapp
$ which psql
$ psql -h localhost
```
** If you want to exit from psql you can type "\q".

## Prepare applications
```sh
$ heroku login
$ heroku git:clone -a todo-list-by-gique.git
$ cd todo-list-by-gique
```
When you clone finish, please you change the datasource at file "application.properties" field "spring.datasource.url" to your datasource.

## Deploy applications
```sh
$ heroku create
$ git push heroku master
```

## Watch application logs
```sh
$ heroku logs --tail
```

## When you change something
```sh
$ git add .
$ git commit -am "push them all"
$ git push heroku master
```

## Try to run and test on local
```sh
$ mvn clean install
$ mvn test cobertura:cobertura
$ heroku local web
```
You can run at url: http://localhost:5000

### Database Schema and example data
```sh
CREATE TABLE IF NOT EXISTS todo (id SERIAL PRIMARY KEY, line_id TEXT NOT NULL,  task TEXT NOT NULL, status  char(10), important char(1), due_date TIMESTAMP NOT NULL, created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL);
INSERT INTO todo (line_id, task, status, important, due_date, created_at, updated_at) VALUES ('id_1_test', 'test task', 'incomplete', '0', now(), now(), now());
INSERT INTO todo (line_id, task, status, important, due_date, created_at, updated_at) VALUES ('id_1_test', 'test task 2', 'completed', '0', now(), now(), now());
```

### Other command
```sh
$ heroku ps:scale web=1
$ heroku open
$ heroku info
```

# Have Fun!!!




