// document.addEventListener()
/*
let markList = [{ w: 'alone', z: "孤独的", desc: 'adj. 单独的；孤独的；独自的' }];


$(document).ready(function () {
    $(".tooltiptext").dblclick(function (event) {
        // $(event.target).prev().children().remove();
        $("ruby[title='Jack']").children().remove();

        // let word = $(event.target).prev().text();
        // console.log(word);
        // $(event.target).prev().children().filter("rt").remove();
        // console.log($(event.target).prev().children().filter("rt").remove())
        // alert($(event.target).prev());
        event.preventDefault();
    });
});

*/

// document.body.style.backgroundColor = 'red';
// let el = document.createElement("div");
// el.textContent = "hello everyone!";
// el.className = "sayhello";
// document.body.appendChild(el);
// console.log("add element");

let map = new Map();
map.set("script", "script");
map.set("page", "page");
map.set("can", "can");
map.set("add", "add");
map.set("document", "document");
map.set("this", "this");
function matchWord(w) {
    // console.log(w + " " + map.get(w.toLowerCase()));
    // return map.get(w.toLowerCase());
    return true;
}


// console.log(articles);
function nav(ul) {
    let el = ul.firstChild;
    if (el) {
        do {
            if (el.nodeType === 1) {
                nav(el);
            } else if (el.nodeType === 3) {
                let v = el.nodeValue.trim();
                v.length > 0 && checkMatch(el, el.nodeValue);
            }
        } while (el = el.nextSibling);
    }
}

function addTextBefore(el, text) {
    let word = document.createTextNode(text);
    el.parentNode.insertBefore(word, el);
};
function addTargetWordBefore(el, text) {
    // let word = document.createTextNode(text);
    // let wordSpan = document.createElement("span");
    // wordSpan.className = "unknown";
    // wordSpan.appendChild(word);

    let span = document.createElement("span");
    let ruby = document.createElement("ruby");
    let word = document.createTextNode(text);
    ruby.appendChild(word);
    let rt = document.createElement("rt");
    let tip = document.createTextNode(text);
    rt.appendChild(tip);
    ruby.append(rt);
    span.append(ruby);
    span.className = "unknown";
    // span.appendChild(word);

    el.parentNode.insertBefore(span, el);
}

function checkMatch(el, v) {
    let replaced = false;
    let wordFrom = 0;
    let outputFrom = 0;
    for (i = 0; i < v.length; i++) {
        let c = v.charAt(i);
        if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
            break;
        }
    }
    wordFrom = i;
    for (j = wordFrom; j < v.length; j++) {
        // 查找单词结束位置
        for (i = j; i < v.length; i++) {
            let c = v.charAt(i);
            if (!(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'))) {
                break;
            }
        }
        let w = v.substring(wordFrom, i);

        if (matchWord(w)) {
            if (wordFrom > outputFrom) {
                addTextBefore(el, v.substring(outputFrom, wordFrom));
            }
            addTargetWordBefore(el, w);
            replaced = true;
            outputFrom = i;
        }

        // 查找下一个单词结束位置
        for (; i < v.length; i++) {
            let c = v.charAt(i);
            if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
                break;
            }
        }
        wordFrom = i;
        j = i;
    }

    // console.log("v.length:" + v.length + " outputFrom: " + outputFrom);
    if (0 < outputFrom && outputFrom < v.length) {
        addTextBefore(el, v.substring(outputFrom, v.length));
        replaced = true;
    }
    if (replaced) {
        el.parentNode.removeChild(el);
    }
}

function checkMatch2(v) {
    let matched = [];
    let ws = v.split(" ");
    console.log(ws);
    ws.forEach(w => {
        if (map.get(w.trim())) {
            matched.push(w.trim());
        }
    });

    return matched
}

window.onload = function () {
    let articles = document.getElementsByTagName("article");
    if (!articles || articles.length == 0) {
        articles = document.getElementsByClassName("article");
    }
    let article;
    // articles = document.getElementsByClassName("article");
    if (articles && articles.length > 0) {
        article = articles[0];
        nav(article);
    } else {
        nav(document.body);
    }



    console.log($(document.body).text());
} 