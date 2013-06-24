encodeHTML = function (source) {
    return String(source)
        .replace(/&/g,'&amp;')
        .replace(/</g,'&lt;')
        .replace(/>/g,'&gt;')
        .replace(/\\/g,'&#92;')
        .replace(/"/g,'&quot;')
        .replace(/'/g,'&#39;');
}; 
//转义影响正则的字符
encodeReg = function (source) {
	return String(source).replace(/([.*+?^=!:${}()|[\]/\\])/g,'\\$1');
};