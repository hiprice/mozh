package main

import (
	"fmt"
	"log"
	"../item"
	"../category"
	"../config"
	"github.com/kataras/iris"
	"../mongo"
)

func main()  {
	fmt.Println("Start Mozhdegani server")
	mongo.Setup()
	registerRoutes()
	registerStaticRoutes()
	iris.Listen(":7777")
	/*
	if err := http.ListenAndServe(":7777", nil); err != nil {
		log.Fatal("failed to start server", err)
	}
	*/
}
func registerStaticRoutes() {
	//thumbnail url
	thumbnailUrl :=  config.Config.StaticUrl + config.Config.ThumbnailUrl
	iris.StaticServe(config.Config.ItemThumbnailImagesPath, thumbnailUrl)
	//full image size
	fullImageSizeUrl :=  config.Config.StaticUrl + config.Config.FullImageUrl
	iris.StaticServe(config.Config.ItemImagesPath, fullImageSizeUrl)

}

func registerRoutes()  {
	item.ListItemHandler()
	listCategories()
	item.NewItemHandler()
	item.DetailItemHandler()
	item.ApproveItemHandler()
}

func listCategories() {
	iris.Get("/category/list",func(ctx *iris.Context)	{
		log.Println("Start serving /categories request")
		categories := category.Categories()
		ctx.JSON(iris.StatusOK,	categories)
	})
}

