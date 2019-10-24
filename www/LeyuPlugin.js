var exec = require('cordova/exec');

const leyu = {
  /**
   * @description: 开启服务
   * @param {type}
   * @return:
   */
  openService: function() {
    exec(() => {}, () => {}, 'LeyuPlugin', 'attemptToBindService', []);
  },
  /**
   * @description: 关闭服务
   * @param {type}
   * @return:
   */
  closeService: function() {
    exec(() => {}, () => {}, 'LeyuPlugin', 'unbindService', []);
  },
  /**
   * @description: 获取状态
   * @param {type}
   * @return:
   */
  getTpEnable: function() {
    return new Promise(resolve => {
      exec(
        success => {
          if (success) {
            resolve(true);
          }
        },
        error => {
          if (error) {
            resolve(false);
          }
        },
        'LeyuPlugin',
        'getTpEnable',
        []
      );
    });
  },
  /**
   * @description: 设置状态
   * @param {type}
   * @return:
   */
  setTpEnable: function(enable) {
    exec(() => {}, () => {}, 'LeyuPlugin', 'setTpEnable', [enable]);
  }
};

module.exports = leyu;
