# Party Schedule Service - Backend for Assembly schedule management and viewing

## Installation

Clone the repo, install dependencies, put config files into place and run the DB setup script.

```
git clone git@github.com:Assembly-WebCrew/pss.git
cd pss
npm install
```

Two files are needed for database connections and setting up db with test data. You'll need config/sequelize.json populated with your MySQL/MariaDB database settings. There is an example file at config/sequelize.json.example for you to use.

You'll also need a file bin/setup-db.sh. There's also example file available as bin/setup-db.sh.example and you'll need to enter the same database settings as you entered into sequelize.json. (TODO: have the setup-db script just fetch credentials from sequelize.json).

After the files are in place, you can run the setup-db.sh script and start developing.

```
chmod u+x bin/setup-db.sh
bin/setup-db.sh
```

### Resetting

If you need to reset the database back to testdata, you can do so.

```
npm run resetdb
```

## Usage

TODO