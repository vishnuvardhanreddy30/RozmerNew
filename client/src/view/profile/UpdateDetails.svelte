<script>
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import TextField from "../../widget/fields/TextField.svelte";
    import NumberField from "../../widget/fields/NumberField.svelte";
    import Button from "../../widget/button/Button.svelte";
    import SessionUtil from "../../util/SessionUtil";
    import proIcon from "../../assets/user-icon.png";
    import Boot from "../../util/Boot";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import Utils from "../../util/Utils";
    import Base from "../../util/Base";
    import Labels from "../../const/Labels";
    import { User } from "../../store/User"

    let userInfo = {};
    let loginUserInfo = {};
    let showDetails = true;
    let labelAlign = Boot.isDesktop() ? "left" : "column";
    let profile;
    let maxLength = 10,
        required = true;

    $: {
        loginUserInfo = SessionUtil.get("info", true);
        let selectedUserId = getUIDFromHash()
        if(selectedUserId){
            showDetails = false;
            fetchUserProfilePic(selectedUserId)
        }else {
            userInfo = loginUserInfo
        }
    }

    function getUIDFromHash() {
        const hash = location.hash; // Get the hash part of the URL
        const queryString = hash.includes('?') ? hash.split('?')[1] : '';
        const params = new URLSearchParams(queryString);
        return params.get('uid');
    }

    function onUpdate() {
        if(Utils.isEmpty(userInfo.firstName) || Utils.isEmpty(userInfo.lastName) || Utils.isEmpty(userInfo.mobileNo)) {
            Utils.alert(Labels.required.mandatory);
            return;
        }

        //to check 10 digits in phone number
        // let mobileNoReg = /^[2-9]\d{2}[2-9]\d{2}\d{4}$/;
        let mobileNoReg = /^[0-9]{10}$/;
        if (!mobileNoReg.test(userInfo.mobileNo)) {
            Utils.alert("Please enter valid phone number");
            return;
        }

        Utils.mask(true);
        let data = {
            firstName: userInfo.firstName,
            lastName: userInfo.lastName,
            mobileNo: userInfo.mobileNo,
        };

        Request.put(
            urlConst.post_user_profile.replace("{userId}", userInfo.userId),
            data,
            onSuccess,
            onFailure,
            onSuccess
        );

        Utils.log(data);
    }

    function onSuccess(resp) {
        Utils.mask();

        if (Utils.isEmpty(resp)) {
            SessionUtil.removeAll();

            return onFailure();
        }

        for (let key in resp) {
            userInfo[key] = resp[key];
        }

        SessionUtil.set("info", userInfo);
        Base.toast("success", Labels.profile.update_cnf, 3000);
    }

    function onFailure(err) {
        Utils.mask();
        Base.toast("danger", Labels.profile.update_fail, 3000);
    }

    function handleFileInput(event) {
        profile = event.target.files[0];
        if (profile) {
            let formdata = new FormData();
            formdata.append("image", profile);
            fetch(urlConst.upload_profile_pic.replace("{loginUserId}", userInfo.userId), {
            method: "POST",
            body: formdata,
        })
            .then((response) => response.text())
            .then((result) => {
                result = JSON.parse(result);
                Base.toast('success', Labels.publish.update_msg);
                fetchUserProfilePic(userInfo.userId)
            })
            .catch((error) => {
                Utils.log(error);
                Utils.alert(Labels.publish.thumbnail_upload_fail, Labels.alert.error);
            });
        }
    }

    function triggerFileInput() {
        document.getElementById('fileInput').click();
    }

    function fetchUserProfilePic(uid) {
        fetch(urlConst.get_user_details.replace("{userId}", uid), {
            method: "GET"
        })
            .then((response) => response.text())
            .then((result) => {
                result = JSON.parse(result);
                if(loginUserInfo.userId == result.userId) {
                    userInfo.imageName = result.imageName
                    SessionUtil.set("info", userInfo)
                    User.set({ userInfo: userInfo });
                } else {
                    userInfo = result
                    showDetails = true
                }
                
            })
            .catch((error) => {
                Utils.log(error);
            });
    }
</script>
{#if showDetails}
<div
    class="profile-details-cont overflow-y flex-cont flex-dir-column flex-vh"
    align="center"
>
    <div class="pro-img-cont" align="center">
        <!-- svelte-ignore a11y-missing-attribute -->
        <img src={userInfo.imageName ? urlConst.get_profile_pic +userInfo.imageName : proIcon} on:click={triggerFileInput} class="profile-image" />
        <!-- <div class="profile-image pointer" on:click={triggerFileInput} style="background-image: url({userInfo.imageName ? urlConst.get_profile_pic +userInfo.imageName : proIcon});"/> -->

        <input type="file" id="fileInput" accept="image/*" style="display: none;" on:change={handleFileInput} />
        {#if loginUserInfo.userId == userInfo.userId}
        <div class="plus-icon" on:click={triggerFileInput}>
            <i class="fa fa-plus-circle pointer"></i> <!-- Font Awesome icon for plus -->
        </div>
        {/if}
    </div>

    <div class="flex-cont pb-1" align="left">
        <TextField
            label={Labels.register.first_name}
            bind:value={userInfo.firstName}
            {labelAlign}
            {required}
        />
    </div>
    <div class="flex-cont pb-1" align="left">
        <TextField
            label={Labels.register.last_name}
            bind:value={userInfo.lastName}
            {labelAlign}
            {required}
        />
    </div>
    <div class="flex-cont pb-1" align="left">
        <NumberField
            label={Labels.register.mob_num}
            bind:value={userInfo.mobileNo}
            {labelAlign}
            {maxLength}
            {required}
        />
    </div>
    {#if loginUserInfo.userId == userInfo.userId}
        <Toolbar ui="plaind">
            <div class="flex-cont" slot="center">
                <Button text={Labels.profile.update} on:click={onUpdate} />
            </div>
        </Toolbar>
    {/if}
</div>
{/if}
<style>
    .profile-details-cont .flex-cont {
        align-content: center;
        width: fit-content;
    }

    .profile-details-cont .pro-img-cont {
        position: relative;
        margin: 20px auto;
        display: flex;
        justify-content: center;
    }

    /* Circular profile image with border */
    .profile-image {
        width: 120px;
        height: 120px;
        border-radius: 50%;
        border: 4px solid #1a9b97; /* Standard color border */
        object-fit: cover;
        cursor: pointer;
    }

    /* Position the plus icon at the bottom-right of the profile image */
    .plus-icon {
        position: absolute;
        bottom: 0;
        right: 0;
        /* font-size: 28px; */
        color: white;
        height: 32px;
        width: 32px;
        background-color: #1a9b97;
        border-radius: 50%;
        /* padding: 4px; */
        cursor: pointer;
        border: 2px solid #1a9b97;
    }

    .profile-details-cont .overlay-icon {
        font-size: 24px;
        color: rgba(255, 255, 255, 0.7);
        pointer-events: none;
    }
</style>
