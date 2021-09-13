const path = require('path');
const express = require('express');
const app = express();
const port = 8080;
const { createProxyMiddleware } = require('http-proxy-middleware');
const cors = require('cors');
app.use(cors({
  'allowedHeaders': ['Content-Type'],
  'origin': '*',
  'preflightContinue': true
}));

let customerApiProxyOptions = {
  target: 'http://gateway.fedev.10.19.2.21.nip.io:30862',
  changeOrigin: true,
  pathRewrite: {
    '^/customers-api': `/`,
  },
  logLevel: 'debug',
  secure: false,
};

const customerApiProxy = createProxyMiddleware(customerApiProxyOptions);

let orderApiProxyOptions = {
  target: 'http://gateway.fedev.10.19.2.21.nip.io:30862',
  changeOrigin: true,
  pathRewrite: {
    '^/orders-api': `/`,
  },
  logLevel: 'debug',
  secure: false,
};

const ordersApiProxy = createProxyMiddleware(orderApiProxyOptions);

let cropsApiProxyOptions = {
  target: 'https://www.growstuff.org',
  changeOrigin: true,
  pathRewrite: {
    '^/crops-api': `/`,
  },
  logLevel: 'debug',
  secure: false,
};

const cropsApiProxy = createProxyMiddleware(cropsApiProxyOptions);

let productApiProxyOptions = {
  target: 'http://gateway.fedev.10.19.2.21.nip.io:30862',
  changeOrigin: true,
  pathRewrite: {
    '^/products-api': `/`,
  },
  logLevel: 'debug',
  secure: false,
};

const productsApiProxy = createProxyMiddleware(productApiProxyOptions);

app.use(express.static(path.join(__dirname, '/dist')));

app.use('/products-api/', productsApiProxy);
app.use('/orders-api/', ordersApiProxy);
app.use('/customers-api/', customerApiProxy);
app.use('/crops-api/', cropsApiProxy);

app.get('*', (req, res) =>{
  res.sendFile(path.join(__dirname + '/dist/index.html'));
});

app.listen(port, () => console.log(`Listening on port ${port}`));
