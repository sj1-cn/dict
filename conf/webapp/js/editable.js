
window.onload = function () {
    console.log("onload")
    document.getElementById("src").addEventListener("paste", function (e) {
        // console.log(e);
        if (!(e.clipboardData && e.clipboardData.items)) {
            return;
        }

        for (var i = 0, len = e.clipboardData.items.length; i < len; i++) {
            var item = e.clipboardData.items[i];
            // console.log("item.kind " + item.kind)
            // console.log(item);
            if (item.kind === "string" && item.type === "text/plain") {
                item.getAsString(function (str) {
                    // console.log("paste " + str);
                    $.post("http://localhost/NLPWords?x=" + Math.random(), { codecontent: str },
                        function (data) {
                            let src = document.getElementById("src")
                            let dest = document.getElementById("dest");
                            src.innerHTML = data;
                            // dest.style = "display:block;";
                            // src.style = "display:none;";
                        }
                    );
                })
                break;
            } else if (item.kind === "file") {
                var pasteFile = item.getAsFile();
                // pasteFile就是获取到的文件
            }
        }
        // e.target.scrollTo(0, 0);
    });

    document.getElementById("fetch").addEventListener("click", function (e) {
        let dest = document.getElementById("dest");
        let src = document.getElementById("src")
        dest.style = "display:none;";
        src.style = "display:block;";
    });

    document.getElementById("remember").addEventListener("click", function (e) {
        hasRemember("who");
    });

    document.getElementById("forget").addEventListener("click", function (e) {
        hasForget("who");
    });

    function hasForget(words) {
        $.ajax("http://localhost/api/userwords/1?x=" + Math.random(), {
            method: 'PUT',
            data: { action: "forget", words: words },
            success: function (data) {
                console.log(data.data);
            },
            dataType: "json"
        });
    }

    function hasRemember(words) {
        $.ajax({
            type: 'PUT',
            url: "http://localhost/api/userwords/1?x=" + Math.random(),
            data: { action: "remember", words: words },
            success: function (data) {
                console.log(data.data);
            },
            dataType: "json"
        });
    }


    
    // dataType: "json",
}

