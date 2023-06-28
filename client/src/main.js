import App from './App.svelte';
import './assets/css/var.css';
import './assets/css/global.css';
import './assets/css/responsive.css';
import "kothing-editor/dist/css/kothing-editor.min.css";
import Router from './util/Router';
import Boot from './util/Boot';


// activate router

Router.activateHistory();


// Add platform config as class to the body
document.body.classList.add('x-' + String(Boot.getDeviceType()).toLowerCase());

const app = new App({
    target: document.getElementById('app')
})

export default app
