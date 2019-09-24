var exec = require('cordova/exec');

const leyu = {
  coolMethod: function(arg0, success, error) {
    exec(success, error, 'LeYu', 'coolMethod', [arg0]);
    console.log('coolMethod, in');
  },

  setTpEnable: function(arg0) {
    console.log(arg0);
    console.log('setTpEnable, in');
    exec(() => {}, () => {}, 'LeYu', 'setTpEnable', [arg0]);
  },

  getTpEnable: function() {
    console.log('getTpEnable, in');
    exec(() => {}, () => {}, 'LeYu', 'getTpEnable', '');
  }
};

module.exports = leyu;

