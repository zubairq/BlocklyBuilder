function(args) {
/*
is_app(true)
control_type("VB")
display_name("3Scale control")
description("This will return the 3Scale control")
base_component_id("rh3scale_control")
load_once_from_file(true)
visibility("PRIVATE")
read_only(true)
properties(
    [
        {
            id:         "width",
            name:       "Width",
            default:    350,
            type:       "Number"
        }
        ,
        {
            id:         "height",
            name:       "Height",
            default:    300,
            type:       "Number"
        }
        ,
        {
            id:         "port",
            name:       "Port",
            default:    1234,
            type:       "Number"
        }
        ,
        {
            id:      "host",
            name:    "Host",
            default: "host.docker.internal",
            type:    "String"
        }
        ,
        {
            id:     "background_color",
            name:   "Background color",
            type:   "String"
        }
        ,
        {
            id:     "container_list",
            name:   "Container list",
            type:   "List"
        }
        ,
        {
            id:         "is_container",
            name:       "Is Container?",
            type:       "Boolean",
            default:    true,
            hidden:     true
        }
        ,
        {
            id:         "getFilteredContainerList",
            pre_snippet:    `await `,
            snippet:    `getFilteredContainerList()`,
            name:       "Get Container List",
            type:       "Action"
        }
    ]
)//properties

logo_url("/driver_icons/rh3scale.png")
*/

    Vue.component("rh3scale_control",{

        props: ["meta", "args","design_mode","refresh", "children"]

        ,



        template:

`<div v-bind:style='"height:100%;width:100%; border: 0px;" +
    "background-color: "+    args["background_color"]  +  ";"'>

    <div v-if="design_mode && (children.length == 0)" style="margin: 10px;">
        <img src="/driver_icons/rh3scale.png" width=100px></src>
        <h3 class="text-center" >Red Hat 3Scale connector</h3>
        The Red Hat 3Scale Connector can be used to query 3Scale, or send
        API requests through the 3Scale gateway
    </div>

    <div v-bind:style='"position:relative;width:100%;height:100%;border: 0px solid gray;background-color: "+    args["background_color"]  +  ";"'>
        <div style="position:absolute;top:0px">
            <slot v-bind:refresh='refresh'>
            </slot>
        </div>
    </div>
</div>`

        ,

        data: function() {
            return {
                msg: "..."
            }
        }

        ,

        mounted: async function() {
            registerComponent(this)

            if (!this.design_mode) {
                var x = await this.readFromDocker()
                this.args.container_list = x
            }
        }
        ,


        methods: {
            readFromDocker: async function() {
                var result = await callFunction(
                {
                    driver_name: "serverDockerStuff",
                    method_name: "serverDockerStuff"
                }
                ,
                {
                    host: this.args.host ,
                    port: this.args.port
                })

                //alert(JSON.stringify(result.value,null,2))
                if (result.value) {
                    return result.value
                }
                return null
            }
            ,


            getFilteredContainerList: async function() {
                var qwe = await this.readFromDocker()
                var newList = []

                for (var aa = 0; aa < qwe.length; aa ++) {

                    var newObject = {
                                        image:          qwe[aa].Image,
                                        privatePort:    qwe[aa].Ports[0].PrivatePort,
                                        publicPort:     qwe[aa].Ports[0].PublicPort,
                                        state:          qwe[aa].State,
                                        status:         qwe[aa].Status
                                    }
                    newList.push(newObject)
                }
                return newList
            }

        }




    })
}
