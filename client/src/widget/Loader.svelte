<script>
    import Labels from "../const/Labels";
    export let loadMaskText = Labels.loadingText;
</script>

<div class="loader-main">
    <div class="l-in f">
        <div class="lds-ring">
            <div class="m-l" />
            <div class="m-l" />
            <div class="m-l" />
            <div class="m-l" />
        </div>
        <div class="l-t">{loadMaskText}</div>
    </div>
</div>

<style>
    .loader-main {
        position: fixed;
        width: 100vw;
        height: 100vh;
        left: 0;
        top: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: rgb(0 0 0 / 40%);
    }
    .l-in {
        background-color: rgb(0 0 0 / 40%);
        padding: 10px;
        border-radius: 10px;
        text-align: center;
    }
    .lds-ring {
        display: inline-block;
        position: relative;
        width: 80px;
        height: 80px;
    }
    .lds-ring .m-l {
        box-sizing: border-box;
        display: block;
        position: absolute;
        width: 64px;
        height: 64px;
        margin: 8px;
        border: 8px solid #fff;
        border-radius: 50%;
        animation: lds-ring 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;
        border-color: #fff transparent transparent transparent;
    }
    .lds-ring .m-l:nth-child(1) {
        animation-delay: -0.45s;
    }
    .lds-ring .m-l:nth-child(2) {
        animation-delay: -0.3s;
    }
    .lds-ring .m-l:nth-child(3) {
        animation-delay: -0.15s;
    }

    .l-t {
        color: var(--white-color);
    }
    @keyframes lds-ring {
        0% {
            transform: rotate(0deg);
        }
        100% {
            transform: rotate(360deg);
        }
    }
</style>
