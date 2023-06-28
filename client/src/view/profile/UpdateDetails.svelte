<script>
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import TextField from "../../widget/fields/TextField.svelte";
    import NumberField from "../../widget/fields/NumberField.svelte";
    import Button from "../../widget/button/Button.svelte";
    import SessionUtil from "../../util/SessionUtil";
    import proIcon from "../../assets/pro-nav-icon.png";
    import Boot from "../../util/Boot";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import Utils from "../../util/Utils";
    import Base from "../../util/Base";
    import Labels from "../../const/Labels";

    let userInfo = {};
    let labelAlign = Boot.isDesktop() ? "left" : "column";
    let maxLength = 10,
        required = true;

    $: {
        userInfo = SessionUtil.get("info", true);
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
</script>

<div
    class="profile-details-cont overflow-y flex-cont flex-dir-column flex-vh"
    align="center"
>
    <div class="pro-img-cont" align="center">
        <!-- svelte-ignore a11y-missing-attribute -->
        <img width="120px" src={proIcon} />
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
    <Toolbar ui="plaind">
        <div class="flex-cont" slot="center">
            <Button text={Labels.profile.update} on:click={onUpdate} />
        </div>
    </Toolbar>
</div>

<style>
    .profile-details-cont .flex-cont {
        align-content: center;
        width: fit-content;
    }

    .profile-details-cont .pro-img-cont {
        margin: 20px auto;
    }
</style>
