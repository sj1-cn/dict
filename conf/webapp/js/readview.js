window.onload = function () {
    let backdrop = document.getElementById("backdrop");
    let table = document.getElementsByTagName("table")[0];
    bindRubyClick();

    document.body.addEventListener("paste", function (e) {
        if (!(e.clipboardData && e.clipboardData.items)) {
            return;
        }
        for (var i = 0, len = e.clipboardData.items.length; i < len; i++) {
            var item = e.clipboardData.items[i];
            // console.log("item.kind " + item.kind)
            // console.log(item);
            if (item.kind === "string" && item.type === "text/plain") {
                item.getAsString(function (str) {
                    // console.log(str);
                    backdrop.innerText = str;
                    // decorater(str);
                    render(str);
                });
                break;
            } else if (item.kind === "file") {
                var pasteFile = item.getAsFile();
                // pasteFile就是获取到的文件
            }
        }
    });

    function decorater(str) {
        let lines = str.split("\n");
        let max = lines.length;
        if (lines.length > 30) {
            max = 30;
        }

        let tbody = document.createElement("tbody");

        {//Decorator first 30lines

            for (i = 0; i < max; i++) {
                tbody.appendChild(decoreateLine(i + 1, lines[i]));
            }

            let oldtbody = tbodyOf(table);

            oldtbody.parentNode.replaceChild(tbody, oldtbody);
        }


        //TODO 后期继续优化，每次执行30行以减少画面等待。
        // setTimeout(() => {
        for (i = 30; i < lines.length; i++) {
            tbody.appendChild(decoreateLine(i + 1, lines[i]));
        }
        // }, 1000);
    }

    function decoreateLine(lineNumber, lineContent) {
        let tdLineNumber = document.createElement("td");
        tdLineNumber.className = "line-number";
        tdLineNumber.setAttribute("value", lineNumber);

        let tdLineContent = document.createElement("td");
        tdLineContent.className = "line-content";
        tdLineContent.innerText = lineContent;

        let tr = document.createElement("tr");
        tr.appendChild(tdLineNumber);
        tr.appendChild(tdLineContent);
        return tr;
    }

    function render(str) {
        $.post("/ana/words?x=" + Math.random(), { codecontent: str },
            function (data) {
                $("#content").html(data);
                // let lines = data.split("<br/>");
                // console.log(lines.length)

                // attachResultToTable(lines, table);
               /* $.each($("ruby"), function () {
                    console.log($(this).text());
                });*/

                bindRubyClick();
                // .onclick(function(e){
                //     console.log(e.target);
                // })
            }
        );
    }

    function bindRubyClick() {
        $("ruby").click(function (e) {
            if (e.target.nodeName === "RUBY") {
                // document.getElementById("audioSource").setAttribute("src","http://media.shanbay.com/audio/us/" + e.target.firstChild.text + ".mp3")
                let audioSource = document.getElementById("audioSource");
                let word = e.target.firstChild.nodeValue.toLowerCase();
                // audioSource.src = "http://media.shanbay.com/audio/us/" + e.target.firstChild.nodeValue.toLowerCase() + ".mp3";
                audioSource.src = "https://sp0.baidu.com/-rM1hT4a2gU2pMbgoY3K/gettts?lan=en&text=" + word + "&spd=2&source=alading"
                let audio = document.getElementById("audio");
                audio.load();
                audio.play();
            }
        });
    }

    function attachResultToTable(lines, table) {
        let tbody = tbodyOf(table);
        let el = tbody.firstChild;
        for (i = 0; i < lines.length; i++) {
            el = firstTagSiblingOf(el, "TR");
            if (el) {
                let lineContent = lineContentOf(el);
                // $(lineContent).html(lines[i]);
                lineContent.innerHTML = lines[i];
                // console.log(lines[i]);
                // console.log(lineContent);
            } else {
                break;
            }
            el = el.nextSibling;
        }
    }


    function tbodyOf(table) {
        let el = table.firstChild;
        while (el && !(el.nodeName === "TBODY")) {
            el = el.nextSibling;
        }
        return el;
    }

    function firstTagSiblingOf(el, tag) {
        while (el && !(el.nodeName === tag)) {
            el = el.nextSibling;
        }
        return el;
    }

    function lineContentOf(tr) {
        let el = tr.firstChild;
        while (el && !(el.nodeName === "TD")) {
            el = el.nextSibling;
        }
        el = el.nextSibling;
        while (el && !(el.nodeName === "TD")) {
            el = el.nextSibling;
        }
        return el;
    }


    // document.getElementById("fetch").addEventListener("click", function (e) {
    //     let dest = document.getElementById("dest");
    //     let src = document.getElementById("src")
    //     dest.style = "display:none;";
    //     src.style = "display:block;";
    // });

    // document.getElementById("remember").addEventListener("click", function (e) {
    //     hasRemember("who");
    // });

    // document.getElementById("forget").addEventListener("click", function (e) {
    //     hasForget("who");
    // });

    // function hasForget(words) {
    //     $.ajax("http://localhost/api/userwords/1?x=" + Math.random(), {
    //         method: 'PUT',
    //         data: { action: "forget", words: words },
    //         success: function (data) {
    //             console.log(data.data);
    //         },
    //         dataType: "json"
    //     });
    // }

    // function hasRemember(words) {
    //     $.ajax({
    //         type: 'PUT',
    //         url: "http://localhost/api/userwords/1?x=" + Math.random(),
    //         data: { action: "remember", words: words },
    //         success: function (data) {
    //             console.log(data.data);
    //         },
    //         dataType: "json"
    //     });
    // }



    // dataType: "json",
}

