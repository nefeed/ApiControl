/*	edit by chb 
 *	需要在body中调用此文件，在head中引用将无法操作document对象 
 *	依靠 IEDebug来控制是否在IE下显示console div,
 *	在浏览器URL中添加#debug即可设置IEDbug的值，例如http://www.ms2.com.cn/userLogin.html#debug
 */
if(!window.console){  
    console = (function(){  
        var instance = null;  
        function Constructor(){  
            
        }  
        Constructor.prototype = {  
           
        }  
        function getInstance(){  
            
            return instance;  
        }  
        return getInstance();  
    })()  
} 