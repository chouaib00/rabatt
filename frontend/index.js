const express = require('express');
const app = express();
const port = process.env.PORT || 3000;
const morgan = require('morgan');
const bodyParser = require('body-parser');
const methodOverride = require('method-override');

app.use(express.static(`${__dirname}/public`));
app.use(morgan('dev'));
app.use(bodyParser.urlencoded({ extended: 'true' }));
app.use(bodyParser.json());
app.use(bodyParser.json({ type: 'application/vnd.api+json' }));
app.use(methodOverride());

app.get('*', (req, res) => {
  res.sendFile(`${__dirname}/public/index.html`);
});

app.listen(port);
console.log(`App listening on port ${port}`);
