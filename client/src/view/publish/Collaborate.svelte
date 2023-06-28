<script>
    import { onMount } from "svelte";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    // import FileField from "../../widget/fields/FileField.svelte";
    // import TextField from "../../widget/fields/TextField.svelte";
    import urlConst from "../../const/Url";
    // import SessionUtil from "../../util/SessionUtil";
    import Request from "../../util/Request";
    import KothingEditor from "kothing-editor";
    import katex from "katex";
    import plugins from "kothing-editor/lib/plugins";
    import Utils from "../../util/Utils";
    import Labels from "../../const/Labels";
    import Base from "../../util/Base";

    let editor, editorEl, postId, details, commentId, questionId, colDetails;
 

    onMount(() => {
        editor = KothingEditor.create("editor_classic", {
            display: "block",
            width: "100%",
            height: innerHeight * 0.77 + "px", //"430px",
            popupDisplay: "full",
            katex: katex,
            plugins: plugins,
            imageUploadSizeLimit: 1e6,
            toolbarItem: []
        });

        handleResize();
        
        setTimeout(()=> {
            editor.setContents(details.content);
            editor.disabled();
            handleResize();
        }, 700)
    });

    function onSuccess(res) {
        Utils.mask();

        postId = res.postId;

        Base.toast('success', Labels.collaborate.add_success);

        Utils.redirectTo("home", {
            pid: postId,
        });
    }

    function onFailure(err) {
        Utils.mask();
        Utils.log("[POST] - Create error");
        Utils.alert(Labels.publish.update_fail, Labels.alert.error);
    }

    function onSave() {
        let text = "";
        if (window.getSelection) {
            text = window.getSelection().toString();
        } else if (document['selection'] && document['selection'].type != "Control") {
            text = document['selection'].createRange().text;
        }

        if(Utils.isEmpty(text)) {
            Utils.alert(Labels.collaborate.collaborate_body, Labels.alert.info);

            return;
        }

        let util = editor.core,
            color = '#a3dfc9',
            newNode = util.util.createElement("SPAN");
        
        newNode.style.backgroundColor = color;

        let id = ''
        if(!Utils.isEmpty(colDetails.commentId)) {
            id += 'cc-' + colDetails.commentId;
            // newNode.setAttribute('cc', colDetails.commentId);
        }

        if(!Utils.isEmpty(colDetails.questionId)) {
            // newNode.setAttribute('qc', colDetails.questionId);
            id += 'cq-' + colDetails.questionId;
        }

        newNode.classList.add('collaborated-content');
        newNode.classList.add(id);
        
        util.nodeChange(newNode, ["background-color"], null, null);
        
        let content = editor.core.getContents();

        createPost(content);
    }

    function createPost(content) {
        Utils.mask(true);

        let data = details || {};

        details.content = content;

        Request.put(
            urlConst.update_post.replace("{postId}", postId),
            data,
            onSuccess,
            onFailure,
            onSuccess
        );

        Utils.log("[Publish] Updating Post");
    }

    function onCancel() {
        Utils.redirectTo("home", {
            pid: postId,
        });
    }

    export function handleResize() {
        setTimeout(() => {
            editor.setOptions({
                height: Utils.calculateAvailableSpace(
                    editorEl.nextElementSibling,
                    150
                ),
            });
            editor.disabled();
        }, 300);
    }

    $: {
        colDetails = window['collaborateDetails'];

        if(colDetails) {
            postId = colDetails.postId;
            details = colDetails.details;
            questionId = colDetails.questionId;
            commentId = colDetails.commentId;
        }

        if(!postId || !details) {
            Utils.redirectTo('home');
        }
    }
</script>

<div class="flex-cont flex-dir-column wh-100-percent collaborate-cont">
    <textarea
        id="editor_classic"
        class="flex-1"
        bind:this={editorEl}
    />
    <Toolbar ui="plain">
        <div class="flex-cont" slot="right">
            <Button
                text={Labels.publish.cancel}
                ui="cancel"
                iconCls="material-icons"
                iconText="close"
                on:click={onCancel}
            />
            <Button
                text={Labels.collaborate.submit}
                iconCls="material-icons"
                iconText="check"
                on:click={onSave}
            />
        </div>
    </Toolbar>
</div>

<style>
    .collaborate-cont {
        padding: 20px;
    }

    :global(.collaborate-cont .kothing-editor-common) {
        display: none !important;
    }

    :global(.collaborate-cont .collaborated-content) {
        user-select: none;
        pointer-events: none;
    }
</style>
