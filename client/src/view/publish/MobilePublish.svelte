<script>
    import { onMount } from "svelte";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import FileField from "../../widget/fields/FileField.svelte";
    import TextField from "../../widget/fields/TextField.svelte";
    import urlConst from "../../const/Url";
    import SessionUtil from "../../util/SessionUtil";
    import Request from "../../util/Request";
    import KothingEditor from "kothing-editor";
    import katex from "katex";
    import plugins from "kothing-editor/lib/plugins";
    import Utils from "../../util/Utils";
    import Labels from "../../const/Labels";
    import Base from "../../util/Base";

    let editor, title, files, imageName, editorEl;

    export let postId;

    onMount(() => {
        editor = KothingEditor.create("editor_modern", {
            display: "block",
            width: "100%",
            height: innerHeight * 0.67 + "px", //"430px",
            popupDisplay: "full",
            katex: katex,
            plugins: plugins,
            imageUploadSizeLimit: 1e6,
            toolbarItem: [
                ["undo", "redo"],
                ["bold", "underline", "italic", "fontColor"],
                ["outdent", "indent", "align"],
            ],
            templates: [
                {
                    name: "Sample Template",
                    html: "<p>HTML source1</p>",
                },
                {
                    name: "Template-2",
                    html: "<p>HTML source2</p>",
                },
            ],
            charCounter: true,
        });

        Utils.alert(Labels.publish.mob_info, Labels.alert.info, function(){
            let userInfo = SessionUtil.get("info", true);
            let url = urlConst.get_user_posts.replace("{userId}", userInfo.userId);
            Request.get(url, null, onGetPostSuccess, ongetPostFailure, onGetPostSuccess);
        });
    });

    function onGetPostSuccess(res) {
        if(res['length'] < 2) {
            Base.toast('success', '* ' + Labels.alert.post_2_feed, 5000);
        }
    }

    function ongetPostFailure(err) {
        Utils.log(err);
    }

    function showSuccessConfirmation() {
        Utils.alert(Labels.publish.update_msg, Labels.alert.success);
    }

    function onSuccess(res) {
        Utils.mask();

        if (Utils.isEmpty(res.postId)) {
            return Utils.log("Post id is null");
        }

        if ((imageName && !files) || !files) {
            Base.toast('success', Labels.publish.update_msg);

            Utils.redirectTo('home');
            return;
        }

        postId = res.postId;

        // upload image
        let formdata = new FormData();
        formdata.append("image", files[0]);

        fetch(urlConst.upload_post_thumbnail.replace("{postId}", postId), {
            method: "POST",
            body: formdata,
            redirect: "follow",
        })
            .then((response) => response.text())
            .then((result) => {
                result = JSON.parse(result);
                imageName = result["imageName"];
                files = null;
                
                Base.toast('success', Labels.publish.update_msg);

                Utils.redirectTo('home');
            })
            .catch((error) => {
                Utils.log(error);
                Utils.alert(Labels.publish.thumbnail_upload_fail, Labels.alert.error);
            });
    }

    function onFailure(err) {
        Utils.mask();
        Utils.log("[POST] - Create error");
        Utils.alert(Labels.publish.update_fail, Labels.alert.error);
    }

    function onSave() {
        if (Utils.isEmpty(title)) {
            return Utils.alert(
                Labels.publish.title_validation,
                Labels.publish.create_err_title
            );
        }

        if (title.length < 3 || title.length > 100) {
            return Utils.alert(
                Labels.publish.title_char_validation,
                Labels.publish.create_err_title
            );
        }
        let content = editor.core.getContents();

        if (Utils.isEmpty(content) || content === "<p><br></p>") {
            return Utils.alert(
                Labels.publish.content_validation,
                Labels.publish.create_err_title
            );
        }

        if (Utils.isEmpty(files) && Utils.isEmpty(imageName)) {
            Utils.confirm(
                Labels.publish.thumbnail_validation,
                Labels.publish.create_warn_title,
                function (id) {
                    if (id === "ok") {
                        return;
                    }

                    createPost(content);
                },
                Labels.publish.yes,
                Labels.publish.later
            );

            return;
        }

        createPost(content);
    }

    function createPost(content) {
        Utils.mask(true);

        let data = {
            content: content,
            title: title,
        };

        let userInfo = SessionUtil.get("info", true);

        if (postId) {
            return updatePost(data);
        }

        Request.post(
            urlConst.create_post.replace("{userId}", userInfo.userId),
            data,
            onSuccess,
            onFailure,
            onSuccess
        );
        Utils.log("[Publish] Creating Post");
    }

    function updatePost(data) {
        Request.put(
            urlConst.update_post.replace("{postId}", postId),
            data,
            onSuccess,
            onFailure,
            onSuccess
        );

        Utils.log("[Publish] Updating Post");
    }

    function deletePost() {
        Utils.confirm(
            Labels.publish.delete_cnf_body,
            Labels.publish.delete_title,
            function (id) {
                if (id === "ok") {
                    Request.delete(
                        urlConst.delete_post_by_id.replace("{postId}", postId),
                        null,
                        (resp) => {
                            location.hash = "publish";
                            postId = null;
                            title = null;
                            files = null;
                            editor.setContents("");
                            Utils.alert(
                                Labels.publish.del_success,
                                Labels.alert.success
                            );

                            Utils.log("[Delete Post] Successful");
                        },
                        (err) => {
                            Utils.log("[Delete Post] Failed");
                            Utils.alert(
                                Labels.publish.del_fail,
                                Labels.alert.error
                            );
                        },
                        onSuccess
                    );
                }
            }
        );
    }

    function onCancel() {
        Utils.redirectTo("home");
    }

    function onPreview() {
        Utils.showImagePreview(
            files && files[0],
            Labels.publish.preview,
            imageName
        );
    }

    function updateTitle(newTitle) {
        title = newTitle;
    }

    $: {
        let params = Utils.getParamsAsObject(location.hash);

        if (!Utils.isEmpty(params.postId)) {
            postId = params.postId;

            // get the publish page gets redirected for update then get the
            // details and fill the content
            Request.get(
                urlConst.get_post_by_id.replace("{postId}", postId),
                null,
                (resp) => {
                    Utils.log("[Post Details] Getting post details");
                    updateTitle(resp.title);

                    editor.setContents(resp.content);
                    imageName = resp.imageName;
                },
                (error) => {
                    Utils.log(
                        "[Post Details] Getting error while post details"
                    );
                },
                onSuccess
            );
        }

        if (files) {
            let size = Utils.getImageSize(files[0].size, true),
                isValid = false;

            if (
                size["unit"] === "MB" ||
                size["unit"] === "bytes" ||
                size["unit"] === "KB"
            ) {
                if (
                    size["unit"] === "bytes" ||
                    size["unit"] === "KB" ||
                    (size["unit"] === "MB" && +size["size"] <= 2)
                ) {
                    isValid = true;
                }
            }
            if (!isValid) {
                Utils.alert(
                    Labels.publish.thumbnail_size_validation,
                    Labels.publish.create_err_title
                );
                files = null;
            }
        }
    }

    export function handleResize() {
        setTimeout(() => {
            if(editor.core.hasFocus) {
                return;
            }

            editor.setOptions({
                height: Utils.calculateAvailableSpace(
                    editorEl.nextElementSibling,
                    200
                ),
            });
        }, 200);
    }
</script>

<div class="flex-cont flex-dir-column wh-100-percent publish-cont">
    <!-- <Toolbar ui="plain"> -->
    <div class="flex-cont flex-dir-column mob-pub-header">
        <TextField
            label={Labels.publish.title}
            labelAlign="column"
            bind:value={title}
        />
        <div class="flex-cont mob-thumb-cont">
            <FileField
                label={(!Utils.isEmpty(files) || !Utils.isEmpty(imageName)) ? Labels.publish.change_thumbnail : Labels.publish.choose_thumbnail}
                labelAlign="left"
                bind:files
            />
            {#if !Utils.isEmpty(files) || !Utils.isEmpty(imageName)}
                <Button
                    iconCls="material-icons"
                    iconText="preview"
                    ui="action"
                    on:click={onPreview}
                />
            {/if}
        </div>
    </div>
    <!-- </Toolbar> -->
    <textarea
        id="editor_modern"
        class="wh-100-percent flex-1"
        bind:this={editorEl}
    />
    <Toolbar ui="plain">
        <div class="flex-cont" slot="right">
            {#if postId}
                <Button
                    text={Labels.publish.delete}
                    ui="delete"
                    iconCls="material-icons"
                    iconText="delete"
                    on:click={deletePost}
                />
            {/if}
            <Button
                text={Labels.publish.cancel}
                ui="cancel"
                iconCls="material-icons"
                iconText="close"
                on:click={onCancel}
            />
            <Button
                text={postId ? Labels.publish.update : Labels.publish.create}
                iconCls="material-icons"
                iconText="edit"
                on:click={onSave}
            />
        </div>
    </Toolbar>
</div>

<style>
    .mob-pub-header {
        padding: 0.2rem 1rem;
    }

    .mob-thumb-cont {
        margin-top: 8px;
    }
</style>
