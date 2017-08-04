import models from '../models'
import fs from 'fs'

process.on('unhandledRejection', error => {
  console.log("A promise was rejected but the error wasn't handled:", error);
})
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
    await models.location.create(location);
  }
  for (const event of events) {
    //console.log(models.event.build(event)); //debug
    await models.event.create(event);
  }
   
  models.sequelize.close();
}