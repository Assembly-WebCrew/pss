import models from '../models'
import fs from 'fs'

insertTestdata();



async function insertTestdata() {
  await models.sequelize.sync( { force: true } )
  const users = JSON.parse(fs.readFileSync('testdata/users.json','utf-8'));
  const parties = JSON.parse(fs.readFileSync('testdata/parties.json','utf-8'));
  const locations = JSON.parse(fs.readFileSync('testdata/locations.json','utf-8'));
  const events = JSON.parse(fs.readFileSync('testdata/events.json','utf-8'));
  
  for (const user of users) {
    var dbuser = models.user.build(user);
    dbuser.password = dbuser.generateHash(user.password);
    await dbuser.save();
  }
  for (const party of parties) {
    await models.party.create(party);
  }
  for (const location of locations) {
    var dblocation = models.location.build(location);
    console.log(dblocation); //debug
    await models.location.create(location);
  }
  for (const event of events) {
    await models.event.create(event);
  }
   
  models.sequelize.close();
}