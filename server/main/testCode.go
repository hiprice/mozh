package main

import (
	"fmt"
	"reflect"
)

type Node struct {
	Id string
	Name string
}

func main()  {
	n:= Node{"1","reza"}
	val := reflect.ValueOf(&n).Elem()

	for i:=0; i< val.NumField(); i++ {
		valueField  := val.Field(i)
		typeField := val.Type().Field(i)
		tag  := typeField.Tag
		fmt.Printf("Field Name: %s,\t Field Value: %v,\t Tag Value: %s\n", typeField.Name, valueField.Interface(), tag.Get("tag_name"))
	}

}
