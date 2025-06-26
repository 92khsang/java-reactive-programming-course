package com.hayes.sec10.assignment.groupby;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@Builder
public class PurchaseOrder {
	String item;
	String category;
	Integer price;
}